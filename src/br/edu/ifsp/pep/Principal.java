package br.edu.ifsp.pep;

import br.edu.ifsp.pep.modelo.Pessoa;
import br.edu.ifsp.pep.modelo.Revisao;
import br.edu.ifsp.pep.modelo.Veiculo;
import br.edu.ifsp.pep.modelo.VeiculoId;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class Principal {

    private static EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("conexaoPU");

    public static void main(String[] args) {

        adicionarPessoa();

        adicionar("Presidente Epitácio", "ABC-1234", "Uno", 2000);
        adicionar("Presidente Prudente", "ABC-4566", "Palio", 2010);
        adicionar("Presidente Epitácio", "ABC-4555", "Fiesta", 1999);
        adicionar("Presidente Prudente", "ABC-4885", "Corsa", 2005);
        adicionar("Maringá", "ABC-0000", "Gol", 2005);

        adicionarRevisao("Manutenção Freio", 250, LocalDate.now(), "Presidente Epitácio", "ABC-1234");
        adicionarRevisao("Manutenção Suspensão", 450, LocalDate.now(), "Presidente Epitácio", "ABC-1234");
        
        adicionarRevisao("Troca de Óleo", 125, LocalDate.now(), "Presidente Prudente", "ABC-4566");
        
        adicionarRevisao("Troca de Óleo", 125, LocalDate.now(), "Presidente Prudente", "ABC-4885");

//        createQuery();
//
//        createNamedQuery();
//
//        createNamedQueryComParametro();
//
//        createNamedQueryChavePrimaria();
//
//        createNativeQuery();
//
//        aggregationFunctions();
//
//        System.out.println("///// aggregationFunctionsWithGroup /////");
//        aggregationFunctionsWithGroup();
        join();
        joinFetch();
    }

    private static void adicionarPessoa() {
        Pessoa p = new Pessoa("João");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

    private static void adicionar(String cidade, String placa, String modelo, Integer anoFabricacao) {

        //Gerencia a entidade (transação, CRUD)        
        EntityManager em = emf.createEntityManager();

        //Inicia uma transação
        em.getTransaction().begin();

        Pessoa p = em.find(Pessoa.class, 1l);

        //Gravar o objeto (veiculo1) no banco de dados (INSERT)
        Veiculo v = new Veiculo();
        v.setCodigo(new VeiculoId(cidade, placa));
        v.setModelo(modelo);
        v.setAnoFabricacao(anoFabricacao);
        v.setProprietario(p);

        em.persist(v);

        //Finaliza a transação
        em.getTransaction().commit();
        em.close();

    }

    private static void adicionarRevisao(String descricao, double valor, LocalDate data, String cidade, String placa) {
        EntityManager em = emf.createEntityManager();
        
        
        em.getTransaction().begin();

        Veiculo veiculo = em.find(Veiculo.class, new VeiculoId(cidade, placa));
        
        Revisao revisao = new Revisao(descricao, valor, data);
        revisao.setVeiculo(veiculo);
        
        em.persist(revisao);
        
        em.getTransaction().commit();
        
        em.close();
        
    }

    private static void createQuery() {

        EntityManager em = emf.createEntityManager();

        //A consulta é semelhante ao SQL (tabelas). Ex. Select * from veiculo v where v.ano_fabricao = 2000;
        //JPQL - baseada na classe (entidade). 
        //Ex1: Select v from Veiculo v where v.anoFabricao = 2000;
        //Ex2: from Veiculo v where v.anoFabricao = 2000;
        TypedQuery<Veiculo> query = em.createQuery("SELECT v FROM Veiculo v WHERE v.anoFabricacao = 2000", Veiculo.class);
        List<Veiculo> carros = query.getResultList();

        if (carros != null) {
            for (Veiculo v : carros) {
                System.out.println(v.getCodigo().getPlaca());
            }
        }
        em.close();

    }

    //Named Query tem melhor performance
    private static void createNamedQuery() {

        EntityManager em = emf.createEntityManager();

        TypedQuery<Veiculo> query = em.createNamedQuery(
                "Veiculo.buscarPeloAnoFabricacao",
                Veiculo.class);
        List<Veiculo> carros = query.getResultList();

        if (carros != null) {
            for (Veiculo v : carros) {
                System.out.println(v.getCodigo().getPlaca());
            }
        }
        em.close();

    }

    private static void createNamedQueryComParametro() {

        EntityManager em = emf.createEntityManager();

        TypedQuery<Veiculo> query = em.createNamedQuery(
                "Veiculo.buscarPelaPlaca", Veiculo.class);
        query.setParameter("placa", "ABC-1234");

        List<Veiculo> carros = query.getResultList();

        if (carros != null) {
            for (Veiculo v : carros) {
                System.out.println(v.getCodigo().getPlaca());
            }
        }
        em.close();

    }

    private static void createNamedQueryChavePrimaria() {

        EntityManager em = emf.createEntityManager();

        TypedQuery<Veiculo> query = em.createNamedQuery(
                "Veiculo.buscarPelaPlacaECidade", Veiculo.class);
        query.setParameter("placa", "ABC-1254");
        query.setParameter("cidade", "Presidente Epitácio");
        try {
            Veiculo v = query.getSingleResult();
            System.out.println(v.getCodigo().getPlaca());
        } catch (NoResultException e) {
            System.out.println("Não encontrou o veículo.");
        }
        em.close();
    }

    private static void createNativeQuery() {
        EntityManager em = emf.createEntityManager();
        Query nativeQuery = em.createNativeQuery(
                "select * from veiculo v");

        List veiculos = nativeQuery.getResultList();
        for (Object obj : veiculos) {

            Object result[] = (Object[]) obj;

            System.out.println("Ano: " + result[0]);
            System.out.println("Modelo: " + result[1]);
            System.out.println("Cidade: " + result[2]);
            System.out.println("Placa: " + result[3]);

            //Quando o tipo é informado no método createNativeQuery.
//            if (obj instanceof Veiculo) {
//                Veiculo v = (Veiculo) obj;
//                System.out.println(v.getModelo());
//            }
        }

        em.close();
    }

    private static void aggregationFunctions() {
        EntityManager em = emf.createEntityManager();

        //COUNT
        TypedQuery<Long> queryCount = em.createQuery(
                "SELECT COUNT(v) FROM Veiculo v",
                Long.class);

        Long result = queryCount.getSingleResult();
        System.out.println("COUNT: " + result);

        //MAX
        TypedQuery<Integer> queryMax = em.createQuery(
                "SELECT MAX(v.anoFabricacao) FROM Veiculo v",
                Integer.class);

        Integer resultAnoFabricacao = queryMax.getSingleResult();
        System.out.println("MAX: " + resultAnoFabricacao);

        //MIN
        TypedQuery<Integer> queryMin = em.createQuery(
                "SELECT MIN(v.anoFabricacao) FROM Veiculo v",
                Integer.class);

        Integer resultMinAnoFabricacao = queryMin.getSingleResult();
        System.out.println("MIN: " + resultMinAnoFabricacao);

        //AVG
        TypedQuery<Double> queryAvg = em.createQuery(
                "SELECT AVG(v.anoFabricacao) FROM Veiculo v",
                Double.class);

        Double resultMedia = queryAvg.getSingleResult();
        System.out.println("AVG: " + resultMedia);

        //SUM
        TypedQuery<Long> querySum = em.createQuery(
                "SELECT SUM(v.anoFabricacao) FROM Veiculo v",
                Long.class);

        Long resultSum = querySum.getSingleResult();
        System.out.println("SUM: " + resultSum);

        //Group
        em.close();
    }

    private static void aggregationFunctionsWithGroup() {
        EntityManager em = emf.createEntityManager();

        //COUNT
        System.out.println("COUNT FUNCTION");
        TypedQuery<Object[]> queryCount = em.createQuery(
                "SELECT v.codigo.cidade, COUNT(v) FROM Veiculo v GROUP BY v.codigo.cidade",
                Object[].class);

        List<Object[]> resultList = queryCount.getResultList();
        for (Object[] obj : resultList) {
            System.out.println(obj[0] + " " + obj[1]);
        }

        //MAX
        System.out.println("MAX FUNCTION");
        TypedQuery<Object[]> queryMax = em.createQuery(
                "SELECT v.codigo.cidade, MAX(v.anoFabricacao) FROM Veiculo v GROUP BY v.codigo.cidade",
                Object[].class);

        resultList = queryMax.getResultList();
        for (Object[] obj : resultList) {
            System.out.println(obj[0] + " " + obj[1]);
        }

        //MIN
        System.out.println("MIN FUNCTION");
        TypedQuery<Object[]> queryMin = em.createQuery(
                "SELECT v.codigo.cidade, MIN(v.anoFabricacao) FROM Veiculo v GROUP BY v.codigo.cidade",
                Object[].class);

        resultList = queryMin.getResultList();
        for (Object[] obj : resultList) {
            System.out.println(obj[0] + " " + obj[1]);
        }

        //AVG
        System.out.println("AVG FUNCTION");
        TypedQuery<Object[]> queryAvg = em.createQuery(
                "SELECT v.codigo.cidade, AVG(v.anoFabricacao) FROM Veiculo v GROUP BY v.codigo.cidade",
                Object[].class);

        resultList = queryAvg.getResultList();
        for (Object[] obj : resultList) {
            System.out.println(obj[0] + " " + obj[1]);
        }
//        
//        //SUM
        System.out.println("SUM FUNCTION");
        TypedQuery<Object[]> querySum = em.createQuery(
                "SELECT v.codigo.cidade, SUM(v.anoFabricacao) FROM Veiculo v GROUP BY v.codigo.cidade",
                Object[].class);

        resultList = querySum.getResultList();
        for (Object[] obj : resultList) {
            System.out.println(obj[0] + " " + obj[1]);
        }

        em.close();
    }

    private static void join() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Veiculo> query = em.createQuery(
                "FROM Veiculo v JOIN v.proprietario p WHERE p.codigo = :codigo", Veiculo.class);
        query.setParameter("codigo", 1);

        List<Veiculo> veiculos = query.getResultList();
        for (Veiculo veiculo : veiculos) {
            System.out.println(veiculo);
        }

        em.close();
    }

    private static void joinFetch() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Veiculo> query = em.createQuery(
                "FROM Veiculo v JOIN FETCH v.proprietario p "
                + "WHERE p.codigo = :codigo", Veiculo.class);
        query.setParameter("codigo", 1);

        List<Veiculo> veiculos = query.getResultList();
        for (Veiculo veiculo : veiculos) {
            System.out.println(veiculo);
        }

        em.close();
    }

}
