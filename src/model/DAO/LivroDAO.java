/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.JOptionPane;
import model.bean.Vendedor;
import DB.Connect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.bean.Livro;

public abstract class LivroDAO implements iDAO<Livro> {

    private final String INSERT = "INSERT INTO vendedor(ISBN, AUTOR, CATEGORIAS, NOME DO LIVRO, QUANTIDADE DE PÁGINAS) VALUES (?, ?, ?, ?)";
    private final String UPDATE = "UPDATE Livro SET ISBN=?, AUTOR=?, CATEGORIAS=?, NOME DO LIVRO=? WHERE QUANTIDADE DE PÁGINAS =?";
    private final String DELETE = "DELETE FROM Livro WHERE AUTOR =?";
    private final String LISTALL = "SELECT * FROM Livro";
    private final String LISTBYPRECO = "SELECT * FROM Livro WHERE CATEGORIAS=?";
    private final String LISTBYNOMEDOLIVRO = "SELECT * FROM Livro WHERE ISBN=?";

    private Connect conn = null;
    private Connection conexao = null;

    @Override
    public Livro inserir(Livro novoLivro) {
        conexao = this.getConnect().connection;
        if (novoLivro != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                transacaoSQL.setString(1, novoLivro.getISBN());
                transacaoSQL.setString(2, novoLivro.getAUTOR());
                transacaoSQL.setString(3, novoLivro.getCATEGORIAS());
                transacaoSQL.setBoolean(4, novoLivro.isStatus());

                transacaoSQL.execute();
                JOptionPane.showMessageDialog(null, "Vendedor cadastrado com sucesso", "Registro inserido", JOptionPane.INFORMATION_MESSAGE);

                try (ResultSet generatedKeys = transacaoSQL.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        novoLivro.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Não foi possível recuperar o ID.");
                    }
                }

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o vendedor no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do Livro não podem estar vazios.", "Livro não informado", JOptionPane.ERROR_MESSAGE);
        }

        return novoLivro;
    }

    @Override
    public Livro atualizar(Livro LivroEditado) {
        conexao = this.getConnect().connection;
        Livro livroEditado = null;
        if (LivroEditado != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(UPDATE);

                transacaoSQL.setString(1, LivroEditado.getISBN());
                transacaoSQL.setString(2, LivroEditado.getAUTOR());
                transacaoSQL.setString(3, LivroEditado.getCATEGORIAS());
                transacaoSQL.setBoolean(4,LivroEditado.isStatus());
                
                transacaoSQL.setString(05, LivroEditado.getCATEGORIAS());

                int resultado = transacaoSQL.executeUpdate();

                if (resultado == 0) {
                    JOptionPane.showMessageDialog(null, "Não foi possível atualizar as informações", "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                JOptionPane.showMessageDialog(null, "Livro atualizado com sucesso", "Registro atualizado", JOptionPane.INFORMATION_MESSAGE);

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o Livro no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do Livro não podem estar vazios.", "Livro não informado", JOptionPane.ERROR_MESSAGE);
        }

        return LivroEditado;
    }

    @Override
    public void excluir(int idILivro) {
        
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este Livro?", "Confirmar exclusão",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        // 0 - Sim  1 - Não
        if(confirmar == 1) {
            return;
        }
        conexao = this.getConnect().connection;
        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(DELETE);
                int idISBN = 0;

                transacaoSQL.setInt(1, idISBN);

                boolean erroAoExcluir = transacaoSQL.execute();

                if (erroAoExcluir) {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir", "Não foi possível excluir as informações", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                JOptionPane.showMessageDialog(null, "Registro excluido", "Livro excluido com sucesso", JOptionPane.INFORMATION_MESSAGE);

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao excluir do Livro no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<Livro> buscarTodos() {
        conexao = this.getConnect().connection;

        ResultSet resultado = null;
        ArrayList<Livro> Livro = new ArrayList<Livro>();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTALL);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {
                    Livro LivroEncontrado = new Livro();

                    LivroEncontrado.setISBN(resultado.getInt("ISBN"));
                    LivroEncontrado.setAUTOR(resultado.getString("AUTOR"));
                    LivroEncontrado.setNOMEDOLIVRO(resultado.getInt("NOME DO LIVRO"));
                    LivroEncontrado.setQUANTIDADEDEPAGINAS(resultado.getString("QUANTIDADES DE PÁGINAS"));
                    LivroEncontrado.setStatus(resultado.getBoolean("status"));
                   
                    Livro.add(LivroEncontrado);
                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedores no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return Livro;
    }

    public Livro buscarPorNOMEDOLIVRO(int NOMEDOLIVRO) {
        conexao = this.getConnect().connection;
        
        ResultSet resultado = null;
        Livro LivroEncontrado = new Livro();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTBYNOMEDOLIVRO);
                transacaoSQL.setInt(1, NOMEDOLIVRO);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    LivroEncontrado.setISBN(resultado.getInt("ISBN"));
                    LivroEncontrado.setPREÇO(resultado.getString("AUTOR"));
                    LivroEncontrado.setCATEGORIAS(resultado.getString("CATEGORIAS"));
                    LivroEncontrado.setNOMEDOLIVRO(resultado.getInt("NOME DO LIVRO"));
                    LivroEncontrado.setStatus(resultado.getBoolean("status"));

                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return LivroEncontrado;
    }

    public Connect getConnect() {
        this.conn = new Connect("root","","NovaLivraria");
        return this.conn;
    }

   
    public Livro buscarPorNOMEDOLIVRO(String NOMEDOLIVRO) {
        conexao = this.getConnect().connection;
        
        ResultSet resultado = null;
        Livro LivroEncontrado = new Livro();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                String LISTBYNOMEDOLIVRO = null;
                transacaoSQL = conexao.prepareStatement(LISTBYNOMEDOLIVRO);
     
                transacaoSQL.setString(1, NOMEDOLIVRO);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    LivroEncontrado.setISBN(resultado.getInt("ISBN"));
                    LivroEncontrado.setAUTOR(resultado.getString("AUTOR"));
                    LivroEncontrado.setCATEGORIAS(resultado.getString("CATEGORIAS"));
                    LivroEncontrado.setNOMEDOLIVRO(resultado.getInt("NOME DO LIVRO"));
                    LivroEncontrado.setStatus(resultado.getBoolean("status"));

                }
                
                Connect.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return LivroEncontrado;
    }

    public void excluir(String nomedolivro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}


