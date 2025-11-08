package org.tricol.supplierchain.exception;

public class StockInsuffisantException extends Exception{
    public StockInsuffisantException(String message) {
        super(message);
    }

    public StockInsuffisantException(String produitReference, String quantiteDisponible, String quantiteDemandee) {
        super(String.format("Stock insuffisant pour le produit %s. Disponible: %s, Demandé: %s",
                produitReference, quantiteDisponible, quantiteDemandee));
    }
}
