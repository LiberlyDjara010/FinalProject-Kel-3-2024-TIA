package service;

import config.Database;
import entities.Kamar;
import entities.Reservasi;
import repository.Repository;

import java.util.List;

public class ServiceImpl implements Service {
    private Repository<Kamar> kamarRepository;
    private Repository<Reservasi> reservasiRepository;

    public ServiceImpl(Repository<Kamar> kamarRepository, Repository<Reservasi> reservasiRepository) {
        this.kamarRepository = kamarRepository;
        this.reservasiRepository = reservasiRepository;
    }

    @Override
    public void tambahKamar(String tipeKamar, double harga) {
        Kamar kamar = new Kamar(Database.nextKamarId++, tipeKamar, harga);
        kamarRepository.add(kamar);
        System.out.println("Kamar berhasil ditambahkan: " + tipeKamar + " (ID: " + kamar.getId() + ")");
    }

    @Override
    public void hapusKamar(int idKamar) {
        Kamar kamar = kamarRepository.findById(idKamar);
        if (kamar != null) {
            kamarRepository.remove(kamar);
            System.out.println("Kamar berhasil dihapus: (ID: " + idKamar + ")");
        } else {
            System.out.println("Kamar dengan ID " + idKamar + " tidak ditemukan.");
        }
    }

    @Override
    public void tampilkanKamarTersedia() {
        List<Kamar> kamarList = kamarRepository.findAll();
        System.out.println("--- Daftar Kamar Tersedia ---");
        for (Kamar kamar : kamarList) {
            if (!kamar.isDipesan()) {
                System.out.println("ID: " + kamar.getId() + ", Tipe: " + kamar.getTipeKamar() + ", Harga: " + kamar.getHarga());
            }
        }
    }

    @Override
    public void bookingKamar(String namaTamu, int idKamar) {
        Kamar kamar = kamarRepository.findById(idKamar);
        if (kamar != null && !kamar.isDipesan()) {
            Reservasi reservasi = new Reservasi(Database.nextReservasiId++, namaTamu, kamar);
            reservasiRepository.add(reservasi);
            kamar.setDipesan(true);
            System.out.println("Reservasi berhasil dilakukan untuk tamu: " + namaTamu + " (ID Reservasi: " + reservasi.getIdReservasi() + ")");
        } else {
            System.out.println("Kamar dengan ID " + idKamar + " tidak tersedia.");
        }
    }

    @Override
    public void checkIn(int idReservasi) {
        Reservasi reservasi = reservasiRepository.findById(idReservasi);
        if (reservasi == null) {
            System.out.println("Reservasi dengan ID " + idReservasi + " tidak ditemukan.");

        }

        if (!reservasi.isPembayaranSelesai()) {
            System.out.println("Check-in gagal: Pembayaran belum selesai untuk reservasi ID: " + idReservasi);
            return;
        }

        if (reservasi.isCheckedIn()) {
            System.out.println("Reservasi dengan ID " + idReservasi + " sudah check-in.");
            return;
        }

        reservasi.setCheckedIn(true);
        System.out.println("Check-in berhasil untuk reservasi ID: " + idReservasi);

    }

    @Override
    public void checkOut(int idReservasi) {
        Reservasi reservasi = reservasiRepository.findById(idReservasi);
        if (reservasi != null && reservasi.isCheckedIn()) {
            // Reset the room's status to available
            reservasi.getKamar().setDipesan(false);

            // Remove the reservation from the repository
            reservasiRepository.remove(reservasi);

            System.out.println("Check-out berhasil untuk reservasi ID: " + idReservasi);
        } else {
            System.out.println("Reservasi dengan ID " + idReservasi + " tidak ditemukan atau belum check-in.");
        }
    }

    @Override
    public void bayarReservasi(int idReservasi) {
        Reservasi reservasi = reservasiRepository.findById(idReservasi);
        if (reservasi == null) {
            System.out.println("Reservasi dengan ID " + idReservasi + " tidak ditemukan.");
            return;
        }

        if (reservasi.isPembayaranSelesai()) {
            System.out.println("Reservasi dengan ID " + idReservasi + " sudah dibayar.");
            return;
        }

        // Default payment method if not specified
        reservasi.setMetodePembayaran(Reservasi.MetodePembayaran.TRANSFER_BANK);

        try {
            reservasi.bayar();
            System.out.println("Pembayaran berhasil untuk reservasi ID: " + idReservasi);
        } catch (RuntimeException e) {
            System.out.println("Pembayaran gagal: " + e.getMessage());
        }
    }

    @Override
    public void bayarReservasi(int idReservasi, Reservasi.MetodePembayaran metodePembayaran) {
        Reservasi reservasi = reservasiRepository.findById(idReservasi);
        if (reservasi == null) {
            System.out.println("Reservasi dengan ID " + idReservasi + " tidak ditemukan.");
            return;
        }

        if (reservasi.isPembayaranSelesai()) {
            System.out.println("Reservasi dengan ID " + idReservasi + " sudah dibayar.");
            return;
        }

        reservasi.setMetodePembayaran(metodePembayaran);

        try {
            reservasi.bayar();
            System.out.println("Pembayaran berhasil untuk reservasi ID: " + idReservasi);
        } catch (RuntimeException e) {
            System.out.println("Pembayaran gagal: " + e.getMessage());
        }
    }
    @Override
    public void lihatReservasi() {
        List<Reservasi> reservasiList = reservasiRepository.findAll();
        System.out.println("--- Daftar Reservasi ---");
        for (Reservasi reservasi : reservasiList) {
            System.out.println("ID Reservasi: " + reservasi.getIdReservasi() +
                    ", Nama Tamu: " + reservasi.getNamaTamu() +
                    ", Tipe Kamar: " + reservasi.getKamar().getTipeKamar() +
                    ", Harga: " + reservasi.getKamar().getHarga() +
                    ", Check-in: " + reservasi.isCheckedIn() +
                    ", Pembayaran: " + (reservasi.isPembayaranSelesai() ? "Selesai" : "Belum"));
        }
    }
}