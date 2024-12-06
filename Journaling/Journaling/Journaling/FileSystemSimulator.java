import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystemSimulator {

    private static final String JOURNAL_FILE = "journal.log";
    private Directory rootDirectory;

    public FileSystemSimulator() {
        rootDirectory = new Directory("root");
    }

    public static void main(String[] args) {
        FileSystemSimulator simulator = new FileSystemSimulator();
        simulator.runShell();
    }

    private void runShell() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Simulador de Sistema de Arquivos - Modo Shell");
            System.out.println("Comandos disponíveis:");
            System.out.println("  create <file|dir> <path> [content] - Cria arquivo ou diretório");
            System.out.println("  delete <file|dir> <path>           - Apaga arquivo ou diretório");
            System.out.println("  rename <file|dir> <old> <new>      - Renomeia arquivo ou diretório");
            System.out.println("  copy <source> <destination>        - Copia arquivo");
            System.out.println("  list <path>                        - Lista conteúdo do diretório");
            System.out.println("  exit                               - Sai do programa");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Saindo...");
                    break;
                }
                processCommand(input);
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private void logOperation(String operation) {
        System.out.println("Operação registrada: " + operation);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JOURNAL_FILE, true))) {
            writer.write(operation);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao gravar no arquivo de log: " + e.getMessage());
        }
    }

    public void copy(String sourcePath, String destinationPath) {
        String[] sourceParts = sourcePath.split("/");
        String[] destinationParts = destinationPath.split("/");

        Directory sourceDir = navigateToDirectory(sourceParts, false);
        if (sourceDir == null) {
            System.err.println("Diretório de origem inválido: " + sourcePath);
            return;
        }

        String sourceName = sourceParts[sourceParts.length - 1];
        File sourceFile = sourceDir.getFileByName(sourceName);

        if (sourceFile != null) {
            Directory destinationDir = navigateToDirectory(destinationParts, true);
            if (destinationDir != null) {
                File copiedFile = new File(sourceFile.getName(), sourceFile.getContent());
                destinationDir.addFile(copiedFile);
                logOperation("COPY_FILE: " + sourcePath + " -> " + destinationPath);
                System.out.println("Arquivo copiado: " + sourcePath + " -> " + destinationPath);
            } else {
                System.err.println("Destino não é um diretório válido: " + destinationPath);
            }
        } else {
            System.err.println("Arquivo de origem não encontrado: " + sourcePath);
        }
    }

    private void processCommand(String input) {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            System.err.println("Comando inválido.");
            return;
        }

        String command = parts[0].toLowerCase();
        switch (command) {
            case "create":
                if (parts.length < 3) {
                    System.err.println("Uso: create <file|dir> <path> [content]");
                    return;
                }
                if (parts[1].equalsIgnoreCase("file")) {
                    String content = parts.length > 3 ? input.substring(input.indexOf(parts[3])) : "";
                    createFile(parts[2], content);
                } else if (parts[1].equalsIgnoreCase("dir")) {
                    createDirectory(parts[2]);
                } else {
                    System.err.println("Tipo inválido: " + parts[1]);
                }
                break;

            case "delete":
                if (parts.length < 3) {
                    System.err.println("Uso: delete <file|dir> <path>");
                    return;
                }
                if (parts[1].equalsIgnoreCase("file")) {
                    deleteFile(parts[2]);
                } else if (parts[1].equalsIgnoreCase("dir")) {
                    deleteDirectory(parts[2]);
                } else {
                    System.err.println("Tipo inválido: " + parts[1]);
                }
                break;

            case "rename":
                if (parts.length < 4) {
                    System.err.println("Uso: rename <file|dir> <old> <new>");
                    return;
                }
                rename(parts[1], parts[2], parts[3]);
                break;

            case "copy":
                if (parts.length < 3) {
                    System.err.println("Uso: copy <source> <destination>");
                    return;
                }
                copy(parts[1], parts[2]);
                break;

            case "list":
                if (parts.length < 2) {
                    System.err.println("Uso: list <path>");
                    return;
                }
                list(parts[1]);
                break;

            default:
                System.err.println("Comando não reconhecido: " + command);
                break;
        }
    }

    public void createFile(String filePath, String content) {
        String[] parts = filePath.split("/");
        Directory currentDir = navigateToDirectory(parts, true);

        if (currentDir != null) {
            currentDir.addFile(new File(parts[parts.length - 1], content));
            logOperation("CREATE_FILE: " + filePath);
            System.out.println("Arquivo criado: " + filePath);
        }
    }

    public void createDirectory(String dirPath) {
        String[] parts = dirPath.split("/");
        Directory currentDir = navigateToDirectory(parts, true);
        if (currentDir != null) {
            currentDir.addDirectory(new Directory(parts[parts.length - 1]));
            logOperation("CREATE_DIR: " + dirPath);
            System.out.println("Diretório criado: " + dirPath);
        }
    }

    public void deleteFile(String filePath) {
        String[] parts = filePath.split("/");
        Directory currentDir = navigateToDirectory(parts, false);
        if (currentDir != null) {
            currentDir.deleteFile(parts[parts.length - 1]);
            logOperation("DELETE_FILE: " + filePath);
            System.out.println("Arquivo apagado: " + filePath);
        }
    }

    public void deleteDirectory(String dirPath) {
        String[] parts = dirPath.split("/");
        Directory currentDir = navigateToDirectory(parts, false);
        if (currentDir != null) {
            currentDir.deleteDirectory(parts[parts.length - 1]);
            logOperation("DELETE_DIR: " + dirPath);
            System.out.println("Diretório apagado: " + dirPath);
        }
    }

public void rename(String type, String oldName, String newName) {
    String[] oldParts = oldName.split("/");
    String[] newParts = newName.split("/");

    Directory currentDir = navigateToDirectory(oldParts, false);

    if (currentDir == null) {
        System.err.println("Diretório não encontrado: " + oldName);
        return;
    }

    if (type.equals("dir")) {
        Directory dir = currentDir.getDirectoryByName(oldParts[oldParts.length - 1]);
        if (dir != null) {
            dir.setName(newParts[newParts.length - 1]);
            logOperation("RENAME_DIR: " + oldName + " -> " + newName);
            System.out.println("Diretório renomeado: " + oldName + " -> " + newName);
        } else {
            System.err.println("Diretório não encontrado: " + oldName);
        }
    } else if (type.equals("file")) {
        File file = currentDir.getFileByName(oldParts[oldParts.length - 1]);
        if (file != null) {
            // Remove o arquivo antigo
            currentDir.deleteFile(oldParts[oldParts.length - 1]);
            // Cria um novo arquivo com o novo nome
            File renamedFile = new File(newParts[newParts.length - 1], file.getContent());
            currentDir.addFile(renamedFile);
            logOperation("RENAME_FILE: " + oldName + " -> " + newName);
            System.out.println("Arquivo renomeado: " + oldName + " -> " + newName);
        } else {
            System.err.println("Arquivo não encontrado: " + oldName);
        }
    } else {
        System.err.println("Tipo inválido: " + type);
    }
}


    public void list(String dirPath) {
        String[] parts = dirPath.split("/");
        Directory dir = navigateToDirectory(parts, false);

        if (dir != null) {
            System.out.println("Conteúdo de " + dirPath + ":");
            for (File file : dir.getFiles()) {
                System.out.println("Arquivo: " + file.getName());
            }
            for (Directory subDir : dir.getDirectories()) {
                System.out.println("Diretório: " + subDir.getName());
            }
        } else {
            System.err.println("Diretório não encontrado: " + dirPath);
        }
    }

    private Directory navigateToDirectory(String[] parts, boolean createIfNotExist) {
        Directory currentDir = rootDirectory;

        for (int i = 1; i < parts.length; i++) {
            currentDir = currentDir.getDirectoryByName(parts[i]);
            if (currentDir == null) {
                if (createIfNotExist) {
                    currentDir = new Directory(parts[i]);
                } else {
                    return null;
                }
            }
        }

        return currentDir;
    }
}

class File {
    private String name;
    private String content;

    public File(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}

class Directory {
    private String name;
    private Map<String, File> files;
    private Map<String, Directory> subDirectories;

    public Directory(String name) {
        this.name = name;
        this.files = new HashMap<>();
        this.subDirectories = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFile(File file) {
        files.put(file.getName(), file);
    }

    public void deleteFile(String fileName) {
        files.remove(fileName);
    }

    public File getFileByName(String fileName) {
        return files.get(fileName);
    }

    public void addDirectory(Directory directory) {
        subDirectories.put(directory.getName(), directory);
    }

    public void deleteDirectory(String dirName) {
        subDirectories.remove(dirName);
    }

    public Directory getDirectoryByName(String dirName) {
        return subDirectories.get(dirName);
    }

    public Collection<File> getFiles() {
        return files.values();
    }

    public Collection<Directory> getDirectories() {
        return subDirectories.values();
    }
}
