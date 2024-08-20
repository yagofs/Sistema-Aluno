package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import entities.Aluno;

public class AlunoJDBC {

    private Connection con;
    private String sql;
    private PreparedStatement pst;

    public AlunoJDBC(Connection con) {
        this.con = con;
    }

    public void salvar(Aluno a) throws SQLException, IOException {
        try {
            sql = "INSERT INTO aluno (nome, sexo, dt_nasc) VALUES (?,?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, a.getNome());
            pst.setString(2, a.getSexo());
            Date dataSql = Date.valueOf(a.getDt_nasc());
            pst.setDate(3, dataSql);
            pst.executeUpdate();
            System.out.println("\nCadastro do aluno realizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public List<Aluno> listar() throws SQLException, IOException {
        List<Aluno> alunos = new ArrayList<>();
        ResultSet rs = null;

        try {
            sql = "SELECT * FROM aluno";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setSexo(rs.getString("sexo"));
                aluno.setDt_nasc(rs.getDate("dt_nasc").toLocalDate());
                alunos.add(aluno);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        }
        return alunos;
    }

    public void apagar(int id) throws SQLException, IOException {
        try {
            sql = "DELETE FROM aluno WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Aluno excluído com sucesso!");
            } else {
                System.out.println("Nenhum aluno encontrado com o ID informado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir aluno: " + e.getMessage());
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public void alterar(Aluno a) throws SQLException, IOException {
        try {
            sql = "UPDATE aluno SET nome = ?, sexo = ?, dt_nasc = ? WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, a.getNome());
            pst.setString(2, a.getSexo());
            Date dataSql = Date.valueOf(a.getDt_nasc());
            pst.setDate(3, dataSql);
            pst.setInt(4, a.getId());
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Aluno atualizado com sucesso!");
            } else {
                System.out.println("Nenhum aluno encontrado com o ID informado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao alterar aluno: " + e.getMessage());
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }
}
