package com.uniplane.service;

import com.uniplane.model.StackDeployment;
import com.uniplane.repository.StackDeploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StackDeploymentService {

    @Autowired
    private StackDeploymentRepository repository;

    @Async
    public void deployStackAsync(Long deploymentId, Map<String, String> values, String stackName) {
        try {
            System.out.println("🚀 Starting deployment for: " + stackName);

            // Absolute paths
            Path templateRoot = Paths.get("/Users/sanjay/UniplaneCloud/uniplane/infra/aws/templates");
            Path renderedDir = Paths.get("/Users/sanjay/UniplaneCloud/uniplane/infra/aws/rendered", stackName);
            Files.createDirectories(renderedDir);
            System.out.println("✅ Created render directory: " + renderedDir.toAbsolutePath());

            List<Path> templateFiles = Files.walk(templateRoot)
                .filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".yaml"))
                .collect(Collectors.toList());


            List<String> renderedPaths = new ArrayList<>();

            for (Path templateFile : templateFiles) {
                System.out.println("🧩 Rendering template: " + templateFile);
                String content = Files.readString(templateFile);
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    content = content.replace("${" + entry.getKey() + "}", entry.getValue());
                }

                Path renderedFile = renderedDir.resolve(templateFile.getFileName());
                Files.writeString(renderedFile, content);
                System.out.println("✅ Rendered to: " + renderedFile.toAbsolutePath());
                renderedPaths.add(renderedFile.toString());
            }

            for (String path : renderedPaths) {
                System.out.println("📦 Applying: " + path);
                ProcessBuilder pb = new ProcessBuilder("kubectl", "apply", "-f", path);
                pb.redirectErrorStream(true);
                Process process = pb.start();
                boolean completed = process.waitFor(60, TimeUnit.SECONDS);
                int exitCode = process.exitValue();
                System.out.println("🔧 kubectl exit code: " + exitCode);
            }

            StackDeployment deployment = repository.findById(deploymentId).orElseThrow();
            deployment.setStatus("READY");
            deployment.setRenderedYamlPaths(String.join(",", renderedPaths));
            deployment.setUpdatedAt(Instant.now());
            repository.save(deployment);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                StackDeployment deployment = repository.findById(deploymentId).orElseThrow();
                deployment.setStatus("FAILED");
                deployment.setUpdatedAt(Instant.now());
                repository.save(deployment);
            } catch (Exception inner) {
                inner.printStackTrace();
            }
        }
    }

    @Async
public void deleteStackAsync(Long deploymentId) {
    try {
        StackDeployment deployment = repository.findById(deploymentId).orElseThrow();
        deployment.setStatus("DELETING");
        deployment.setUpdatedAt(Instant.now());
        repository.save(deployment);

        if (deployment.getRenderedYamlPaths() == null || deployment.getRenderedYamlPaths().isBlank()) {
            System.out.println("⚠️ No rendered YAMLs found.");
            deployment.setStatus("DELETED");
            repository.save(deployment);
            return;
        }

        // Sort YAMLs in dependency-aware order
        List<String> paths = new ArrayList<>(Arrays.asList(deployment.getRenderedYamlPaths().split(",")));
        paths.sort(Comparator.comparingInt(path -> {
            if (path.contains("ec2-instance")) return 0;
            if (path.contains("sg-rule")) return 1;
            if (path.contains("sg-template")) return 2;
            if (path.contains("subnet")) return 3;
            if (path.contains("vpc")) return 4;
            return 99;
        }));

        for (String path : paths) {
            System.out.println("🧾 Deleting: " + path);
            Path yamlPath = Paths.get(path);
            if (!Files.exists(yamlPath)) {
                System.out.println("⚠️ Skipping missing file: " + path);
                continue;
            }

            ProcessBuilder pb = new ProcessBuilder("kubectl", "delete", "-f", path, "--wait=true");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (Scanner scanner = new Scanner(process.getInputStream())) {
                while (scanner.hasNextLine()) {
                    System.out.println("kubectl >> " + scanner.nextLine());
                }
            }

            int exitCode = process.waitFor();
            System.out.println("🔧 Exit code for " + path + ": " + exitCode);
        }

        // Poll Crossplane to confirm full deletion
        boolean allDeleted = false;
        for (int i = 0; i < 10; i++) {  // wait up to 60 seconds
            ProcessBuilder check = new ProcessBuilder("kubectl", "get", "managed", "-A");
            Process proc = check.start();
            String output = new String(proc.getInputStream().readAllBytes());
            if (!output.contains("demo-vpc") &&
                !output.contains("demo-ec2") &&
                !output.contains("demo-subnet") &&
                !output.contains("demo-security-group")) {
                allDeleted = true;
                break;
            }
            Thread.sleep(6000);
        }

        deployment.setStatus(allDeleted ? "DELETED" : "DELETE_PENDING");
        deployment.setUpdatedAt(Instant.now());
        repository.save(deployment);

    } catch (Exception e) {
        e.printStackTrace();
        try {
            StackDeployment deployment = repository.findById(deploymentId).orElseThrow();
            deployment.setStatus("DELETE_FAILED");
            deployment.setUpdatedAt(Instant.now());
            repository.save(deployment);
        } catch (Exception inner) {
            inner.printStackTrace();
        }
    }
}

    

    
}
