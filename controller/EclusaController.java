package controller;

import com.mycompany.view.AtualizarCapitaoView;
import com.mycompany.view.AtualizarView;
import com.mycompany.view.BalsaView;
import com.mycompany.view.CapitaoView;
import com.mycompany.view.CargueiroView;
import com.mycompany.view.EmbarEclusaView;
import com.mycompany.view.ExcluirCapView;
import com.mycompany.view.GerenciarCapitaoView;
import com.mycompany.view.PetroleiroView;
import com.mycompany.view.PrecoView;
import com.mycompany.view.RemoverEmbarcacao;
import com.mycompany.view.TurismoView;
import model.Eclusa;
import com.mycompany.view.View;
import exception.TamanhoIncompativelException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.Balsa;
import model.Cargueiro;
import model.NavioTuristico;
import model.Capitao;
import model.Embarcacao;
import model.Petroleiro;
import model.Tamanho;
import factory.EmbarcacaoFactory;
import java.util.HashMap;
import java.util.Map;

public class EclusaController {
    private Eclusa model;
    private View view;
    private BalsaView balsaView;
    private PetroleiroView petroView;
    private TurismoView turiView;
    private CargueiroView cargView;
    private RemoverEmbarcacao removeView;
    private EmbarEclusaView tela= new EmbarEclusaView(this);
    private AtualizarView telaAtualizar  = new AtualizarView(this);
    private PrecoView telaPreco = new PrecoView(this);
    private CapitaoView capitaoView = new CapitaoView(this);
    private GerenciarCapitaoView gerenCapView = new GerenciarCapitaoView(this);
    private ExcluirCapView excluirCapView = new ExcluirCapView(this);
    private AtualizarCapitaoView atualizarCapView = new AtualizarCapitaoView(this);
    private Map<Integer, Capitao> repositorioCapitaes;

    public EclusaController(){}
    public EclusaController(Eclusa model, View view){
        this.model=model;
        this.view=view;
        this.repositorioCapitaes = new HashMap<>();
    }
    public void evento(java.awt.event.ActionEvent evt) {
        String comando = evt.getActionCommand();

        switch (comando) {
            // Controles da Eclusa
            case "Abrir":
                this.abrir();
                break;
            case "Fechar":
                this.fechar();
                break;
            case "Secar":
                this.secar();
                break;
            case "Encher":
                this.encher();
                break;
            case "Atualizar Dados Eclusa":
                this.atualizarEclusa();
                break;
            

            // Abrir Janelas
            case "ExcluirCap":
                this.telaExcluirCap();
                break;
            case "Atualizar Eclusa":
                this.telaAtualizarEclusa();
                break;
            case "Atualizar Preços":
                this.telaAtualizarPrecos();
                break;
            case"Editar":
                this.telaEditar();
                break;
            case "Cadastrar Novo":
                this.telaCapitao();
                break;
            case "Cargueiro":
                this.abrirViewCargueiro(); // Abre a janela
                break;
            case "Petroleiro":
                this.abrirViewPetroleiro();
                break;
            case "Turismo":
                this.abrirViewTurismo();
                break;
            case "Balsa":
                this.abrirViewBalsa();
                break;

            // Ações de Salvar (Botão "Adicionar" dentro das janelas)
            
            case "addBalsa":
                this.adicionarEmbarcacao("balsa"); 
                break;
            case "addCargueiro":
                this.adicionarEmbarcacao("cargueiro");
                break;
            case "addTurismo":
                this.adicionarEmbarcacao("turismo");
                break;
            case "addPetroleiro":
                this.adicionarEmbarcacao("petroleiro");
                break;

            // Botões "Voltar"
            case "VoltarBalsa":
                this.voltarBalsa();
                break;
            case "VoltarCarg":
                this.voltarCarg();
                break;
            case "VoltarTuri":
                this.voltarTuri();
                break;
            case "VoltarPetro":
                this.voltarPetro();
                break;
            case "Voltar":
                this.voltar(); // Voltar da tela de visualização
                break;
            case "VoltarCap":
                this.voltarCap(); // Voltar da tela de visualização
                break;
            case "VoltarPreco":
                this.voltarPreco();
                break;
            case "VoltarEclusa":
                this.voltarEclusa(); 
                break;
            case "VoltarGereCap":
                this.voltarGereCap();
                break;
            case "VoltarExcluir":
                this.voltarExcluir();
                break;
            case "VoltarEditar":
                this.voltarEditarCap();
                break;
            case "Gerenciar Capitães":
                telaGerenCap();
                break;
            case "Cadastrar": 
                try {
                    this.cadastrarCapitao(
                        capitaoView.getNome(), 
                        capitaoView.getNumLicenca(), 
                        capitaoView.getContato()
                    );

                    this.atualizarListaNaViewCapitao(); 

                    JOptionPane.showMessageDialog(capitaoView, "Capitão cadastrado com sucesso!");
                    capitaoView.limparCampos();
                    
                    gerenCapView.setListaCaptaes(listarCapitaes());
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(capitaoView, e.getMessage());
                }
                break;
            case"ExcluirCapitao":
                try {
                    // Pega o objeto selecionado na ComboBox da tela de Capitão
                    Capitao selecionado = excluirCapView.getCapitaoSelecionado();
                    this.removerCapitao(selecionado);
                    JOptionPane.showMessageDialog(excluirCapView, "Capitão removido com sucesso!");
                    gerenCapView.setListaCaptaes(listarCapitaes());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(excluirCapView, "Erro: " + e.getMessage());
                }
                break;
            case"AtualizarCap":
                try {
                    model.Capitao selecionado = atualizarCapView.getCapitaoSelecionado();
                    
                    String novoNome = atualizarCapView.getNome();
                    String novaLicenca = atualizarCapView.getNumLicenca(); 
                    String novoContato = atualizarCapView.getContato();
                    
                    this.atualizarCapitao(selecionado, novoNome, novaLicenca, novoContato);
                    javax.swing.JOptionPane.showMessageDialog(atualizarCapView, "Dados atualizados com sucesso!");
                    gerenCapView.setListaCaptaes(listarCapitaes());
                    atualizarCapView.limparCampos();
                    
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(atualizarCapView, "Erro: " + e.getMessage());
                }
                break;
            case "AtualizarPreco":
                this.atualizarPreco();
                break;
            case "Atualizar":
                this.atualizarEclusa();
                break;
            // Visualização e Remoção
            case "EmbarcacaonaEclusa":
                this.mostrarEmbarcacao();
                break;
            case "Remover da Fila":
                this.abrirRemove();
                break;
            case "SIM":
                this.remove();
                break;
            case "NÂO": // Cuidado com o acento no código original
                this.naoRemoverEmbarcacao();
                break;

            default:
                System.out.println("Comando não reconhecido: " + comando);
        }
    }
    
    
    
    public void voltar(){
        tela.setVisible(false);
    }
    
    public void naoRemoverEmbarcacao(){
        removeView.setVisible(false);
    }
    
    public void abrirRemove(){
        if(model.tamanhoFila()==0){
            JOptionPane.showMessageDialog(view, "Fila Vazia");
        }else{
            removeView= new RemoverEmbarcacao(this);
            this.mostrarEmbarcacao(true);
            removeView.setVisible(true);
        }
    }
    
    
    public void remove(){
        if(model.tamanhoFila()>0){
            view.atualizartotalApuradoRemove();
            view.atualizaFila(model.tamanhoFila(),model.tempo());
            if(model.tamanhoFila()==0){
                view.sentidoProxima("Fila vazia");
            }
            removeView.setVisible(false);
            JOptionPane.showMessageDialog(view, "A última embarcação saiu da fila");
        }
    }
    
    public void voltarBalsa(){
        balsaView.setVisible(false);
    }
    public void voltarPetro(){
        petroView.setVisible(false);
    }public void voltarTuri(){
        turiView.setVisible(false);
    }public void voltarCarg(){
        cargView.setVisible(false);
    }
    public void voltarCap(){
        capitaoView.setVisible(false);
    }
    public void voltarPreco(){
        telaPreco.setVisible(false);
    }
    public void voltarEclusa(){
        telaAtualizar.setVisible(false);
    }
    public void voltarGereCap(){
        gerenCapView.setVisible(false);
    }
    public void voltarExcluir(){
        excluirCapView.setVisible(false);
    }
    public void voltarEditarCap(){
        atualizarCapView.setVisible(false);
    }
    
    
    public void adicionarEmbarcacao(String nomeEmbarcacao) {
        try {
            switch (nomeEmbarcacao.toLowerCase()) {
                case "balsa":
                    this.addEmbarcacao("balsa",
                            balsaView.getComprimento(), balsaView.getLargura(),
                            balsaView.getOrigem(), balsaView.getDestino(),
                            balsaView.getPais(), balsaView.getCodigoIdentificacao(),
                            balsaView.getCapitaoSelecionado(), balsaView.getSentido(),
                            balsaView, 
                            balsaView.getCarga(), balsaView.getPeso());
                    break; 

                case "cargueiro":
                    this.addEmbarcacao("cargueiro",
                            cargView.getComprimento(), cargView.getLargura(),
                            cargView.getOrigem(), cargView.getDestino(),
                            cargView.getPais(), cargView.getCodigoIdentificacao(),
                            cargView.getCapitaoSelecionado(), cargView.getSentido(),
                            cargView, 
                            cargView.getContainers());
                    break;

                case "petroleiro":
                    this.addEmbarcacao("petroleiro",
                            petroView.getComprimento(), petroView.getLargura(),
                            petroView.getOrigem(), petroView.getDestino(),
                            petroView.getPais(), petroView.getCodigoIdentificacao(),
                            petroView.getCapitaoSelecionado(), petroView.getSentido(),
                            petroView, // View correta
                            petroView.getLitros());
                    break;

                case "turismo":
                    this.addEmbarcacao("turismo",
                            turiView.getComprimento(), turiView.getLargura(),
                            turiView.getOrigem(), turiView.getDestino(),
                            turiView.getPais(), turiView.getCodigoIdentificacao(),
                            turiView.getCapitaoSelecionado(), turiView.getSentido(),
                            turiView, // View correta
                            turiView.getPassageiros(), turiView.getCabine());
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de embarcação desconhecido: " + nomeEmbarcacao);
            }

        } catch (NumberFormatException e) {
            // Captura erro se o usuário digitou texto em campos de número
            JOptionPane.showMessageDialog(null, "Por favor, insira valores numéricos válidos.");
        } catch (NullPointerException e) {
            // Captura erro se alguma view não foi inicializada
            JOptionPane.showMessageDialog(null, "Erro interno: Janela não inicializada.");
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        }
    }

    public void atualizarEclusa(){
        try {
            float comprimentoValor = Float.parseFloat(telaAtualizar.getCampoComprimento());
            float larguraValor = Float.parseFloat(telaAtualizar.getCampoLargura());
            float capacidadeMaximaValor = Float.parseFloat(telaAtualizar.getCampoCapacidadeMaxima());
            float capacidadeMinimaValor = Float.parseFloat(telaAtualizar.getCampoCapacidadeMinima());
            float vazaoValor = Float.parseFloat(telaAtualizar.getCampoVazao());

            if (comprimentoValor < 0 || larguraValor < 0 || capacidadeMaximaValor < 0 || capacidadeMinimaValor < 0 ||
                vazaoValor < 0) {
                view.mostrarErro();
            } else {
                this.atualizarEclusa(telaAtualizar.getCampoComprimento(), telaAtualizar.getCampoLargura(), telaAtualizar.getCampoCapacidadeMaxima(),
                        telaAtualizar.getCampoCapacidadeMinima(), telaAtualizar.getCampoVazao());
                telaAtualizar.atuaTempo(model.tempo());
                view.atualizaFila(model.tamanhoFila(),model.tempo());
            }
        } catch (NumberFormatException e) {
            view.mostrarErro();
        }
    }
    
    public void atualizarPreco() {
        try {

            float precoCargueiroValor = Float.parseFloat(telaPreco.getCampoPrecoCargueiro());
            float precoPetroleiroValor = Float.parseFloat(telaPreco.getCampoPrecoPetroleiro());
            float precoTurismoValor = Float.parseFloat(telaPreco.getCampoPrecoTurismo());
            float precoBalsaValor = Float.parseFloat(telaPreco.getCampoPrecoBalsa());
            float precoConteiner = Float.parseFloat(telaPreco.getPrecoConteiner());
            float precoLitro = Float.parseFloat(telaPreco.getPrecoLitro());
            float precoPassageiro = Float.parseFloat(telaPreco.getPrecoPassageiro());
            float precoCabine = Float.parseFloat(telaPreco.getPrecoCabine());
            float precoKgCarga = Float.parseFloat(telaPreco.getPrecoKgCarga());

            if (precoCargueiroValor < 0 || precoPetroleiroValor < 0 || precoTurismoValor < 0 || 
                precoBalsaValor < 0 || precoConteiner < 0 || precoPassageiro < 0 || 
                precoCabine < 0 || precoKgCarga < 0 || precoLitro < 0) {

                JOptionPane.showMessageDialog(telaPreco, "Erro: Os preços não podem ser negativos!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                
            } else {

                telaPreco.setAtualizou();

                JOptionPane.showMessageDialog(telaPreco, "Preços atualizados com sucesso!");

            }
            
        } catch (NumberFormatException e) {
            // Lógica de Erro de Digitação
            telaPreco.setNaoAtualizou();
            
            // POP-UP DE ERRO (Texto em vez de número)
            JOptionPane.showMessageDialog(telaPreco, "Erro: Verifique se todos os campos contêm apenas números.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void telaAtualizarEclusa(){
        telaAtualizar.setVisible(true);
    }
    public void telaAtualizarPrecos(){
        telaPreco.setVisible(true);
    }
    public void telaCapitao(){
        capitaoView.setVisible(true);
    }
    public void abrirViewPetroleiro(){
        petroView=new PetroleiroView(this);
        if (repositorioCapitaes != null && !repositorioCapitaes.isEmpty()) {
            model.Capitao[] lista = repositorioCapitaes.values().toArray(new model.Capitao[0]);
            petroView.atualizarComboCapitaes(lista);
        }
        petroView.setVisible(true);
    }
    public void telaGerenCap(){
        gerenCapView.setListaCaptaes(listarCapitaes());
        gerenCapView.setVisible(true);
    }
    public void telaExcluirCap(){
        this.atualizarListaNaViewCapitao();
        excluirCapView.setVisible(true);
    }

    public void telaEditar(){
        this.atualizarListaNaViewCapitao();
        atualizarCapView.setVisible(true);
    }
    
    public void abrirViewBalsa(){
        balsaView=new BalsaView(this);
        if (repositorioCapitaes != null && !repositorioCapitaes.isEmpty()) {
            model.Capitao[] lista = repositorioCapitaes.values().toArray(new model.Capitao[0]);
            balsaView.atualizarComboCapitaes(lista);
        }
        balsaView.setVisible(true);
    }
    
    public void abrirViewCargueiro(){
        cargView=new CargueiroView(this);
        if (repositorioCapitaes != null && !repositorioCapitaes.isEmpty()) {
             model.Capitao[] lista = repositorioCapitaes.values().toArray(new model.Capitao[0]);
            cargView.atualizarComboCapitaes(lista);
        }
        cargView.setVisible(true);
    }
    
    public void abrirViewTurismo(){
        turiView=new TurismoView(this);
            if (repositorioCapitaes != null && !repositorioCapitaes.isEmpty()) {
            model.Capitao[] lista = repositorioCapitaes.values().toArray(new model.Capitao[0]);
            turiView.atualizarComboCapitaes(lista);
        }
        turiView.setVisible(true);
    }
    
    // Método Genérico para adicionar qualquer embarcação
    public void addEmbarcacao(String tipo, float comprimento, float largura, 
                              String origem, String destino, String pais, int cI, 
                              Capitao capitao, String sentido, 
                              java.awt.Window viewParaFechar, Object... extras) {
        try {
            // 1. A Factory cria a embarcação correta baseada no 'tipo'
            Embarcacao novaEmbarcacao = EmbarcacaoFactory.criar(
                tipo, 
                model.getTamanho(), 
                comprimento, largura, 
                origem, destino, pais, cI, 
                capitao, sentido, 
                extras // Passa os parâmetros extras (peso, conteineres, etc.)
            );

            // 2. Tenta adicionar ao modelo
            if (!model.setEmbarcacoes(cI, novaEmbarcacao)) {
                JOptionPane.showMessageDialog(viewParaFechar, "Já existe uma embarcação com esse ID na fila.");
                return;
            }

            // 3. Atualiza a interface (Lógica comum a todos)
            view.atualizaFila(model.tamanhoFila(), model.tempo());
            view.atualizartotalApurado();

            // Fecha a janelinha específica (BalsaView, CargueiroView, etc.)
            if (viewParaFechar != null) {
                viewParaFechar.setVisible(false);
            }

            // 4. Atualiza o sentido do fluxo na tela principal
            if (model.getFila().get(0).getSentido().equals("Subir")) {
                view.sentidoProxima("RIO--->MAR");
            } else {
                view.sentidoProxima("MAR--->RIO");
            }

        } catch (TamanhoIncompativelException e) {
            JOptionPane.showMessageDialog(viewParaFechar, "Tamanho incompatível com a eclusa!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewParaFechar, "Erro: " + e.getMessage());
        }
    }
    
    
    public void atualizarEclusa(String comprimento, String largura, String capacidadeMaxima, String capacidadeMinima,
            String vazao) {
        if(Float.parseFloat(comprimento)!=model.getTamanho().getComprimento()){
            model.getTamanho().setComprimento(Float.parseFloat(comprimento));
        }
        if(Float.parseFloat(largura)!=model.getTamanho().getLargura()){
            model.getTamanho().setLargura(Float.parseFloat(largura));
        }
        if(Float.parseFloat(capacidadeMaxima)!=model.getCapacidadeMaxima()){
            if(Float.parseFloat(capacidadeMaxima)<model.getCapacidadeMinima()){
                JOptionPane.showMessageDialog(view, "Capacidade Maxima não pode ser menor que a Minima");
            }else{
                model.setCapacidadeMaxima(Float.parseFloat(capacidadeMaxima));
            }
        }
        if(Float.parseFloat(capacidadeMinima)!=model.getCapacidadeMinima()){
            if(Float.parseFloat(capacidadeMinima)>model.getCapacidadeMaxima()){
                JOptionPane.showMessageDialog(view, "Capacidade Minima não pode ser maior que a Maxima");
            }else{
                model.setCapacidadeMinima(Float.parseFloat(capacidadeMinima));
            }
        }
        if(Float.parseFloat(vazao)!=model.getVazao()){
            model.setVazao(Float.parseFloat(vazao));
        }
        if(Float.parseFloat(vazao)<0||Float.parseFloat(capacidadeMinima)<0||Float.parseFloat(capacidadeMaxima)<0||Float.parseFloat(largura)<0||Float.parseFloat(comprimento)<0){
            view.mostrarErro();
        }
    }
    
    
    public void abrir(){
        if (model.getStatus().equals("seca")||model.getStatus().equals("cheia")){
            model.setPorta(true);
            view.recado("Eclusa aberta");
            if(model.getOcupada()==false){
                 if(model.getFila().get(0).getSentido().equals("Subir")&& model.getStatus().equals("seca")){
                     view.sentido("RIO--->MAR");
                     model.getFila().get(0).entrar(model);
                     view.atualizaFila(model.tamanhoFila(),model.tempo());
                        if(model.tamanhoFila()==0){
                             view.sentidoProxima("Fila vazia");
                         }
                 }if(model.getFila().get(0).getSentido().equals("Descer")&& model.getStatus().equals("cheia")){
                     view.sentido("RIO<---MAR");
                     model.getFila().get(0).entrar(model);
                     view.atualizaFila(model.tamanhoFila(),model.tempo());
                     if(model.tamanhoFila()==0){
                             view.sentidoProxima("Fila vazia");
                      }
                 }
                 else{
                     if(model.getFila().get(0).getSentido().equals("Subir")){
                        view.sentidoProxima("RIO--->MAR");
                    }else{
                        view.sentidoProxima("RIO<---MAR");
                    }
                 }
            }else{
                if((model.getEmbarcacaoNaEclusa().getSentido().equals("Subir")&& model.getStatus().equals("cheia"))||(model.getEmbarcacaoNaEclusa().getSentido().equals("Descer")&& model.getStatus().equals("seca"))){
                    model.getEmbarcacaoNaEclusa().sair(model);
                    view.sentido(" ");
                }
                
  
            }
            view.atualizaFila(model.tamanhoFila(),model.tempo());
        }
    }
    public void fechar(){
        if (model.getStatus().equals("seca")||model.getStatus().equals("cheia")){
            model.setPorta(false);
            view.recado("Eclusa fechada");
            view.atualizaFila(model.tamanhoFila(),model.tempo());
        }
    }
    
    public void encher(){
        if(model.getPorta()==false&&model.getStatus().equals("seca")){
            model.setStatus("Enchendo");
            this.carregarBarra();
            view.status(model.getStatus());
            view.atualizaFila(model.tamanhoFila(),model.tempo());
            
        }else if(model.getStatus().equals("Enchendo")){
            view.recado("A Eclusa já está enchendo");
        }else if(model.getStatus().equals("Secando")){
            view.recado("A Eclusa está secando nesse momento");
        }else if(model.getPorta()==false&&model.getStatus().equals("cheia")){
            view.recado("A Eclusa ja esta cheia");
        }
        else{
            view.recado("Feche a eclusa primeiro para encher ");
        }
    }
    public  void secar(){
        if(model.getPorta()==false&&model.getStatus().equals("cheia")){
            model.setStatus("Secando");
            this.carregarBarra();
            view.status(model.getStatus());
            view.atualizaFila(model.tamanhoFila(),model.tempo());
        }else if(model.getStatus().equals("Secando")){
            view.recado("A Eclusa já está Secando");
        }else if(model.getStatus().equals("Enchendo")){
            view.recado("A Eclusa está enchendo nesse momento");
        }
        else if(model.getPorta()==false&&model.getStatus().equals("seca")){
            view.recado("A Eclusa ja esta seca");
        }else{
            view.recado("feche a eclusa primeiro para secar");
        }
    }
    public void mostrarEmbarcacao(){
        if(model.getEmbarcacaoNaEclusa()!=null){
            if(model.getEmbarcacaoNaEclusa()instanceof Balsa){
                Balsa balsa=(Balsa)model.getEmbarcacaoNaEclusa();
                tela.textoEmbarcacao(balsa.mensagemEmbarcacao());
                tela.nomeEmbarcacao("Balsa");
            }if(model.getEmbarcacaoNaEclusa()instanceof Cargueiro){
                Cargueiro carg=(Cargueiro)model.getEmbarcacaoNaEclusa();
                tela.textoEmbarcacao(carg.mensagemEmbarcacao());
                tela.nomeEmbarcacao("Navio Cargueiro");
            }if(model.getEmbarcacaoNaEclusa()instanceof NavioTuristico){
                NavioTuristico turi=(NavioTuristico)model.getEmbarcacaoNaEclusa();
                tela.textoEmbarcacao(turi.mensagemEmbarcacao());
                tela.nomeEmbarcacao("Navio Turistico");
            }if(model.getEmbarcacaoNaEclusa()instanceof Petroleiro){
                Petroleiro petro=(Petroleiro)model.getEmbarcacaoNaEclusa();
                tela.textoEmbarcacao(petro.mensagemEmbarcacao());
                tela.nomeEmbarcacao("Navio Petroleiro");
            }
            tela.setVisible(true);
        }
        
    }
    
    public void mostrarEmbarcacao(boolean remove){
        if(model.getUltimaNaFila()!=null){
            if(model.getUltimaNaFila()instanceof Balsa){
                Balsa balsa=(Balsa)model.getUltimaNaFila();
                removeView.textoEmbarcacao(balsa.mensagemEmbarcacao());
                removeView.nomeEmbarcacao("Balsa");
            }if(model.getUltimaNaFila()instanceof Cargueiro){
                Cargueiro carg=(Cargueiro)model.getUltimaNaFila();
                removeView.textoEmbarcacao(carg.mensagemEmbarcacao());
                removeView.nomeEmbarcacao("Navio Cargueiro");
            }if(model.getUltimaNaFila()instanceof NavioTuristico){
                NavioTuristico turi=(NavioTuristico)model.getUltimaNaFila();
                removeView.textoEmbarcacao(turi.mensagemEmbarcacao());
                removeView.nomeEmbarcacao("Navio Turistico");
            }if(model.getUltimaNaFila()instanceof Petroleiro){
                Petroleiro petro=(Petroleiro)model.getUltimaNaFila();
                removeView.textoEmbarcacao(petro.mensagemEmbarcacao());
                removeView.nomeEmbarcacao("Navio Petroleiro");
            }
        }
        
    }
    
     
    public void carregarBarra(){
        final float tempoTotal = model.tempo()*60000;
        final float intervalo = tempoTotal / 100; 

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep((long) intervalo);
                        final int valor = i; 
                        if(valor==100){
                            view.status(model.getStatus());
                            if(model.getStatus().equals("Enchendo")){
                                model.setStatus("cheia");
                                view.status("Cheia");
                            }else{
                                view.status("Seca");
                                model.setStatus("seca");
                            }
                            
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                view.barra(valor);
                            }
                        });

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public void cadastrarCapitao(String nome, String licencaTexto, String contato) throws Exception {
        // 1. Validação de campos vazios
        if (nome == null || nome.trim().isEmpty() || 
            licencaTexto == null || licencaTexto.trim().isEmpty() || 
            contato == null || contato.trim().isEmpty()) {
            throw new Exception("Todos os dados do capitão são obrigatórios.");
        }

        try {
            // 2. Conversão da licença
            int numLicenca = Integer.parseInt(licencaTexto);

            if (numLicenca <= 0) {
                throw new Exception("O número da licença deve ser positivo.");
            }

            // --- A CORREÇÃO ESTÁ AQUI ---
            // Verifica se a licença JÁ EXISTE no mapa antes de salvar
            if (repositorioCapitaes.containsKey(numLicenca)) {
                throw new Exception("Erro: Já existe um capitão cadastrado com a licença " + numLicenca + "!");
            }
            // ----------------------------

            // 3. Salva se não existir
            repositorioCapitaes.put(numLicenca, new model.Capitao(nome, numLicenca, contato));

        } catch (NumberFormatException e) {
            throw new Exception("A licença do capitão deve conter apenas números.");
        }
    }
    
    public String listarCapitaes() {
        if (repositorioCapitaes.isEmpty()) {
            return "Nenhum capitão cadastrado.";
        }

        StringBuilder lista = new StringBuilder();
        
        for (Capitao c : repositorioCapitaes.values()) {
            lista.append("Nome: ").append(c.getNome())
                 .append(" | Licença: ").append(c.getNumLicenca())
                 .append(" | Contato: ").append(c.getContato())
                 .append("\n------------------------------------------------------\n");
        }
        
        return lista.toString();
    }
    public void removerCapitao(Capitao capitao) throws Exception {
        if (capitao == null) {
            throw new Exception("Selecione um capitão na lista para remover.");
        }
        
        // Remove do mapa usando a licença (chave)
        if (repositorioCapitaes.containsKey(capitao.getNumLicenca())) {
            repositorioCapitaes.remove(capitao.getNumLicenca());
            this.atualizarListaNaViewCapitao(); // Atualiza a combobox visualmente
        } else {
            throw new Exception("Capitão não encontrado no sistema.");
        }
    }
    
    private void atualizarListaNaViewCapitao() {
        // Cria a lista atualizada do Map
        if (repositorioCapitaes != null) {
            model.Capitao[] lista = repositorioCapitaes.values().toArray(new model.Capitao[0]);

            // 1. Atualiza a tela de EXCLUIR (se estiver aberta/criada)
            if (excluirCapView != null) {
                 excluirCapView.atualizarComboCapitaes(lista);
            }

            // 2. Atualiza a tela de EDITAR (se estiver aberta/criada)
            if (atualizarCapView != null) {
                 atualizarCapView.atualizarComboCapitaes(lista);
            }
        }
    }
    
    public void atualizarCapitao(model.Capitao capitaoSelecionado, String novoNome, String novaLicencaTexto, String novoContato) throws Exception {
        if (capitaoSelecionado == null) {
            throw new Exception("Selecione um capitão para atualizar.");
        }
        
        // Validação de campos vazios
        if (novoNome == null || novoNome.trim().isEmpty() || 
            novaLicencaTexto == null || novaLicencaTexto.trim().isEmpty() ||
            novoContato == null || novoContato.trim().isEmpty()) {
            throw new Exception("Todos os campos (Nome, Licença e Contato) são obrigatórios.");
        }

        int novaLicenca;
        try {
            novaLicenca = Integer.parseInt(novaLicencaTexto);
            if (novaLicenca <= 0) throw new Exception("A licença deve ser positiva.");
        } catch (NumberFormatException e) {
            throw new Exception("A licença deve ser um número válido.");
        }

        int antigaLicenca = capitaoSelecionado.getNumLicenca();

        // --- LÓGICA DE TROCA DE LICENÇA (A parte crítica) ---
        if (novaLicenca != antigaLicenca) {
            
            // 1. Verifica se a nova licença já está a ser usada por OUTRO capitão
            if (repositorioCapitaes.containsKey(novaLicenca)) {
                throw new Exception("Erro: A licença " + novaLicenca + " já pertence a outro capitão!");
            }

            // 2. Remove o capitão da chave antiga
            repositorioCapitaes.remove(antigaLicenca);

            // 3. Atualiza o objeto com o novo número
            capitaoSelecionado.setNumLicenca(novaLicenca);

            // 4. Insere o capitão na nova chave
            repositorioCapitaes.put(novaLicenca, capitaoSelecionado);
            
            System.out.println("Licença alterada de " + antigaLicenca + " para " + novaLicenca);
        }
        // ----------------------------------------------------

        // Atualiza os outros dados
        capitaoSelecionado.setNome(novoNome);
        capitaoSelecionado.setContato(novoContato);
        
        // Atualiza a lista visual na tela
        this.atualizarListaNaViewCapitao();
    }
}
