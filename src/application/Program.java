package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import jdbc.AlunoJDBC;
import jdbc.db;
import entities.Aluno;

public class Program {

    public static void main(String[] args) throws IOException {
        
        Scanner console = new Scanner(System.in);
        Connection con = null;
        try {
            con = db.getConexao();
            int opcao = 0;
            
            do {
                try {
                    System.out.println("####### Menu #######"
                                    + "\n1 - Cadastrar"
                                    + "\n2 - Listar"
                                    + "\n3 - Alterar"
                                    + "\n4 - Excluir"
                                    + "\n5 - Sair");
                    System.out.print("\n\tOpção: ");
                    opcao = Integer.parseInt(console.nextLine());
                    
                    AlunoJDBC acao = new AlunoJDBC(con);
                    
                    switch(opcao) {
                        case 1:
                            cadastrarAluno(console, acao);
                            break;
                        case 2:
                            listarAlunos(acao);
                            break;
                        case 3:
                            alterarAluno(console, acao);
                            break;
                        case 4:
                            excluirAluno(console, acao);
                            break;
                        case 5:
                            System.out.println("Conexão Encerrada");
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente.");
                            break;
                    }
                    
                } catch (NumberFormatException e) {
                    System.out.println("Erro: Entrada inválida. Por favor, insira um número.");
                }
                
            } while (opcao != 5); 
            
        } catch (SQLException e) {
            System.out.println("Erro de conexão com o banco de dados: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar a conexão: " + e.getMessage());
                }
            }
            console.close();
        }
    }

    private static void cadastrarAluno(Scanner console, AlunoJDBC acao) {
        try {
            Aluno a = new Aluno();
            
            System.out.println("\n ### Cadastrar Aluno ### \n");
            
            System.out.print("Nome: ");
            a.setNome(console.nextLine());
            
            System.out.print("Sexo: ");
            a.setSexo(console.nextLine());
        
            System.out.print("Data de Nascimento (dd/MM/yyyy): ");
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            a.setDt_nasc(LocalDate.parse(console.nextLine(), formato));
            
            acao.salvar(a);
            
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    private static void listarAlunos(AlunoJDBC acao) {
        try {
            System.out.println("\n ### Lista de Alunos ### \n");
            
            List<Aluno> alunos = acao.listar();
            if (alunos.isEmpty()) {
                System.out.println("Nenhum aluno cadastrado.");
            } else {
                for (Aluno aluno : alunos) {
                    System.out.println("ID: " + aluno.getId());
                    System.out.println("Nome: " + aluno.getNome());
                    System.out.println("Sexo: " + aluno.getSexo());
                    System.out.println("Data de Nascimento: " + aluno.getDt_nasc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    System.out.println("-------------------------");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        }
    }

    private static void alterarAluno(Scanner console, AlunoJDBC acao) {
        try {
            Aluno a = new Aluno();
            
            System.out.println("\n ### Alterar Aluno ### \n");
            
            System.out.print("ID do Aluno: ");
            a.setId(Integer.parseInt(console.nextLine()));
            
            System.out.print("Novo Nome: ");
            a.setNome(console.nextLine());
            
            System.out.print("Novo Sexo: ");
            a.setSexo(console.nextLine());
            
            System.out.print("Nova Data de Nascimento (dd/MM/yyyy): ");
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            a.setDt_nasc(LocalDate.parse(console.nextLine(), formato));
            
            acao.alterar(a);
            
        } catch (Exception e) {
            System.out.println("Erro ao alterar aluno: " + e.getMessage());
        }
    }

    private static void excluirAluno(Scanner console, AlunoJDBC acao) {
        try {
            System.out.println("\n ### Excluir Aluno ### \n");
            
            System.out.print("Digite o ID do aluno a ser excluído: ");
            int id = Integer.parseInt(console.nextLine());
            
            acao.apagar(id);
            
        } catch (Exception e) {
            System.out.println("Erro ao excluir aluno: " + e.getMessage());
        }
    }

}
