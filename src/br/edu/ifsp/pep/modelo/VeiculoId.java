package br.edu.ifsp.pep.modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author aluno
 */
@Embeddable
public class VeiculoId implements Serializable {

    @Column(name = "cidade", length = 45)
    private String cidade;

    @Column(name = "placa", length = 8)
    private String placa;

    public VeiculoId() {
    }

    public VeiculoId(String cidade, String placa) {
        this.cidade = cidade;
        this.placa = placa;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.cidade);
        hash = 41 * hash + Objects.hashCode(this.placa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VeiculoId other = (VeiculoId) obj;
        if (!Objects.equals(this.cidade, other.cidade)) {
            return false;
        }
        return Objects.equals(this.placa, other.placa);
    }

    @Override
    public String toString() {
        return "VeiculoId{" + "cidade=" + cidade + ", placa=" + placa + '}';
    }
    
    

}
