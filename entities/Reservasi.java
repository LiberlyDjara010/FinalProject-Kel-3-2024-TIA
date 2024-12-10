package entities;

public class Reservasi {
    private int idReservasi;
    private String namaTamu;
    private Kamar kamar;
    private boolean checkedIn;
    private boolean pembayaranSelesai;
    private MetodePembayaran metodePembayaran;

    public enum MetodePembayaran {
        TRANSFER_BANK,
        E_WALLET,
        KARTU_KREDIT
    }

    public Reservasi(int idReservasi, String namaTamu, Kamar kamar) {
        this.idReservasi = idReservasi;
        this.namaTamu = namaTamu;
        this.kamar = kamar;
        this.checkedIn = false;
        this.pembayaranSelesai = false;
        this.metodePembayaran = null;
    }

    public int getIdReservasi() {
        return idReservasi;
    }

    public String getNamaTamu() {
        return namaTamu;
    }

    public Kamar getKamar() {
        return kamar;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public boolean isPembayaranSelesai() {
        return pembayaranSelesai;
    }

    public void setPembayaranSelesai(boolean pembayaranSelesai) {
        this.pembayaranSelesai = pembayaranSelesai;
    }
    public MetodePembayaran getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(MetodePembayaran metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
    public void bayar() {
        if (metodePembayaran == null) {
            throw new IllegalStateException("Metode pembayaran belum dipilih");
        }

        switch (metodePembayaran) {
            case TRANSFER_BANK:
                bayarTransferBank();
                break;
            case E_WALLET:
                bayarEWallet();
                break;
            case KARTU_KREDIT:
                bayarKartuKredit();
                break;
        }
        setPembayaranSelesai(true);
    }

    protected void bayarTransferBank() {
        // Simulate bank transfer verification
        if (validateBankPayment()) {
            System.out.println("Pembayaran reservasi ID " + idReservasi + " dengan Transfer Bank berhasil");
        } else {
            throw new RuntimeException("Pembayaran Transfer Bank gagal");
        }
    }

    protected void bayarEWallet() {
        // Simulate e-wallet payment verification
        if (validateEWalletPayment()) {
            System.out.println("Pembayaran reservasi ID " + idReservasi + " dengan E-Wallet berhasil");
        } else {
            throw new RuntimeException("Pembayaran E-Wallet gagal");
        }
    }

    protected void bayarKartuKredit() {
        // Simulate credit card payment verification
        if (validateCreditCardPayment()) {
            System.out.println("Pembayaran reservasi ID " + idReservasi + " dengan Kartu Kredit berhasil");
        } else {
            throw new RuntimeException("Pembayaran Kartu Kredit gagal");
        }
    }

    // Simulated validation methods (you can replace these with actual payment gateway integrations)
    private boolean validateBankPayment() {
        // In a real system, this would interact with a bank's payment API
        return true;
    }

    private boolean validateEWalletPayment() {
        // In a real system, this would interact with an e-wallet payment gateway
        return true;
    }

    private boolean validateCreditCardPayment() {
        // In a real system, this would interact with a credit card payment gateway
        return true;
    }
}