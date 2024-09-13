package br.edu.ifsp.pep.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author aluno
 */
@Entity
@Table(name = "veiculo")
@NamedQueries(value = {
    @NamedQuery(name = "Veiculo.buscarPeloAnoFabricacao", query = "SELECT v FROM Veiculo v WHERE v.anoFabricacao = 2000"),
    @NamedQuery(name = "Veiculo.buscarPelaPlaca", query = "FROM Veiculo v WHERE v.codigo.placa = :placa"),
    @NamedQuery(name = "Veiculo.buscarPelaPlacaECidade", query = "FROM Veiculo v WHERE v.codigo.placa = :placa AND v.codigo.cidade = :cidade")
})
public class Veiculo implements Serializable {

    @EmbeddedId
    private VeiculoId codigo;

    @Column(name = "modelo", length = 30)
    private String modelo;

    @Column(name = "ano_fabricacao")
    private int anoFabricacao;

    @ManyToOne
    @JoinColumn(name = "pessoa_codigo", nullable = false)
    private Pessoa proprietario;

    public VeiculoId getCodigo() {
        return codigo;
    }

    public void setCodigo(VeiculoId codigo) {
        this.codigo = codigo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Pessoa getProprietario() {
        return proprietario;
    }

    public void setProprietario(Pessoa proprietario) {
        this.proprietario = proprietario;
    }

    @Override
    public String toString() {
        return "Veiculo{" + "codigo=" + codigo + ", modelo=" + modelo + ", anoFabricacao=" + anoFabricacao + ", proprietario=" + proprietario + '}';
    }

}
