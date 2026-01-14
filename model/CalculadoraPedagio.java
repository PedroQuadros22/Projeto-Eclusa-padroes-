/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Pedro
 */

import com.mycompany.view.PrecoView;
import java.util.HashMap;
import java.util.Map;
import strategy.*; // Importa todas as estratégias de calculo

public class CalculadoraPedagio {
    
    private Map<String, CalculoPedagioStrategy> estrategias;
    private PrecoView telaPreco;

    public CalculadoraPedagio(PrecoView telapreco) {
        estrategias = new HashMap<>();
        // Inicializa as regras de cobrança 
        estrategias.put("cargueiro", new CalculoCargueiro());
        estrategias.put("petroleiro", new CalculoPetroleiro());
        estrategias.put("turismo", new CalculoTurismo());
        estrategias.put("balsa", new CalculoBalsa());
        this.telaPreco=telapreco;
        
    }


    // Em model/CalculadoraPedagio.java

    public float calcularTarifa(Embarcacao embarcacao, float tarifaBase, String tipo) {

        if (tipo != null && estrategias.containsKey(tipo)) {
            CalculoPedagioStrategy estrategia = estrategias.get(tipo);

            try {
                switch (tipo) {
                    case "cargueiro":
                        String textoContainer = telaPreco.getPrecoConteiner();
                        float precoPorContainer = Float.parseFloat(textoContainer);

                        return estrategia.calcular(tarifaBase, embarcacao, precoPorContainer);

                    case "petroleiro":
                        String textoLitro = telaPreco.getPrecoLitro();
                        float precoPorLitro = Float.parseFloat(textoLitro);

                        return estrategia.calcular(tarifaBase, embarcacao, precoPorLitro);

                    case "turismo":
                        String textoPassageiro = telaPreco.getPrecoPassageiro();
                        String textoCabine = telaPreco.getPrecoCabine();

                        float precoPorPassageiro = Float.parseFloat(textoPassageiro);
                        float precoPorCabine = Float.parseFloat(textoCabine);

                        return estrategia.calcular(tarifaBase, embarcacao, precoPorPassageiro, precoPorCabine);

                    case "balsa":
                        String textoKg = telaPreco.getPrecoKgCarga();
                        float precoPorKg = Float.parseFloat(textoKg);

                        return estrategia.calcular(tarifaBase, embarcacao, precoPorKg);

                    default:
                        return estrategia.calcular(tarifaBase, embarcacao);
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Algum campo de preço na tela contém texto inválido ou está vazio.");
                // Em caso de erro na tela, calcula apenas a tarifa base (fallback)
                return estrategia.calcular(tarifaBase, embarcacao);
            }
        }

        return tarifaBase;
    }
}