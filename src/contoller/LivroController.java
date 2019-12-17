/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contoller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.DAO.LivroDAO;
import model.bean.Livro;

/**
 *
 * @author USUARIO
 */
public class LivroController {
  

    private Livro LivroSelecionado;
    private List<Livro> tabelaDeLivro;
    private LivroDAO vDAO;

    public LivroController() {
        vDAO = new LivroDAO() {
          
            @Override
            public Livro buscarPorId(int id) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    public void listarTodos(DefaultTableModel modeloTabela) {
        modeloTabela.setNumRows(0);
        List<Livro> listaLivro= vDAO.buscarTodos();

       
    }

    public void listarPorISBN(DefaultTableModel modeloTabela, int ISBN) {
        modeloTabela.setNumRows(0);
        Livro vendedorBuscado = vDAO.buscarPorId(ISBN);
        
        }
    
    public void listarPorNOMEDOLIVRO(DefaultTableModel modeloTabela, int NOMEDOLIVRO) {
        modeloTabela.setNumRows(0);
        Livro LivroBuscado = vDAO.buscarPorNOMEDOLIVRO(NOMEDOLIVRO);
         }
    
    public void salvar(DefaultTableModel modeloTabela, Livro Livro, boolean novo ) {
        if( novo ) {
            vDAO.inserir(Livro);
        } else {
            vDAO.atualizar(Livro);
        }
        this.listarTodos(modeloTabela);
    }
    
    public void excluir(DefaultTableModel modeloTabela, Livro Livro ) {
        System.out.println("Excluindo Livro No.: " + Livro.getNOMEDOLIVRO());
        if( Livro.getNOMRDOLIVRO() != 0 ) {
            vDAO.excluir(Livro.getNOMEDOLIVRO());
        }
    }

    public void listarPorCATEGORIA(DefaultTableModel tabelaModelo, String CATEGORIA) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}


