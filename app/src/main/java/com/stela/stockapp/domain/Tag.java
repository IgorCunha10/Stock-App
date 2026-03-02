package com.stela.stockapp.domain;

import androidx.annotation.NonNull;

import com.grotg.hpp.otglibrary.param.EpcBean;

/**
 * Representa uma Tag RFID dentro do domínio da aplicação.
 * <p>
 * Essa classe estende {@link EpcBean} (fornecida pelo SDK da leitora RFID)
 * e adiciona semântica, encapsulamento e métodos utilitários,
 * sem alterar a implementação original do SDK.
 * </p>
 *
 * <h3>Responsabilidades:</h3>
 * <ul>
 *     <li>Fornecer getters e setters com nomes semânticos</li>
 *     <li>Facilitar o uso no padrão MVVM</li>
 *     <li>Centralizar regras de negócio relacionadas à Tag RFID</li>
 * </ul>
 *
 * <h3>Mapeamento de campos do SDK:</h3>
 * <pre>
 * strepc   -> epc (Electronic Product Code)
 * strNo    -> serialNumber
 * strCount -> readCount
 * intRssi  -> rssi (força do sinal)
 * </pre>
 */
public class Tag extends EpcBean {

    /**
     * Construtor padrão.
     * Cria uma Tag vazia.
     */
    public Tag() {
    }

    /**
     * Construtor para criação de uma Tag a partir de dados.
     *
     * @param epc          Código EPC da Tag RFID
     * @param serialNumber Número de série da Tag
     * @param readCount    Quantidade de leituras
     * @param rssi         Intensidade do sinal da leitura
     */
    public Tag(String epc, String serialNumber, int readCount, int rssi) {
        this.strepc = epc;
        this.strNo = serialNumber;
        this.strCount = readCount;
        this.intRssi = rssi;
    }

    public Tag(@NonNull EpcBean epcBean) {
        this.strepc = epcBean.strepc != null ? epcBean.strepc : "";
        this.strNo = epcBean.strNo != null ? epcBean.strNo : "";
        this.strCount = epcBean.strCount;
        this.intRssi = epcBean.intRssi;
    }

    /**
     * Retorna o EPC (Electronic Product Code) da Tag RFID.
     */
    public String getEpc() {
        return strepc;
    }

    /**
     * Define o EPC da Tag RFID.
     */
    public void setEpc(String epc) {
        this.strepc = epc;
    }

    /**
     * Retorna o número de série da Tag.
     */
    public String getSerialNumber() {
        return strNo;
    }

    /**
     * Define o número de série da Tag.
     */
    public void setSerialNumber(String serialNumber) {
        this.strNo = serialNumber;
    }

    /**
     * Retorna quantas vezes essa Tag foi lida.
     */
    public int getReadCount() {
        return strCount;
    }

    /**
     * Define quantas vezes essa Tag foi lida.
     */
    public void setReadCount(int readCount) {
        this.strCount = readCount;
    }


    /**
     * Retorna o RSSI (Received Signal Strength Indicator).
     * Quanto maior o valor, mais forte o sinal da leitura.
     */
    public int getRssi() {
        return intRssi;
    }

    /**
     * Define o RSSI (intensidade do sinal).
     */
    public void setRssi(int rssi) {
        this.intRssi = rssi;
    }

    /**
     * Indica se a Tag possui um EPC válido.
     */
    public boolean hasValidEpc() {
        return strepc != null && !strepc.trim().isEmpty();
    }

    /**
     * Indica se a leitura possui um sinal forte o suficiente.
     *
     * @param minRssi valor mínimo aceitável de RSSI
     * @return true se o sinal for considerado forte
     */
    public boolean hasStrongSignal(int minRssi) {
        return intRssi >= minRssi;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "epc='" + strepc + '\'' +
                ", serialNumber='" + strNo + '\'' +
                ", readCount=" + strCount +
                ", rssi=" + intRssi +
                '}';
    }

    public void incrementReadCount() {
        this.strCount++;
    }
}
