class Pembeli(
    val idPembeli: String,
    val nama: String,
    uangTunaiAwal: Double
) {
    var uangTunai: Double = uangTunaiAwal
        private set

    var poinMember: Int = 0
        private set

    fun bayar(total: Double): Boolean {
        return if (total > 0 && uangTunai >= total) {
            uangTunai -= total
            true
        } else {
            false
        }
    }

    fun tambahPoin(poin: Int = 10) {
        if (poin > 0) {
            poinMember += poin
        }
    }

    fun infoPembeli() {
        println("Pembeli: $nama | Uang Tunai: Rp${"%.0f".format(uangTunai)} | Poin Member: $poinMember")
    }
}

class Barang(
    val kodeBarang: String,
    val namaBarang: String,
    val harga: Double,
    stokAwal: Int
) {
    var stok: Int = stokAwal
        private set

    fun cekStok(jumlah: Int): Boolean {
        return jumlah > 0 && stok >= jumlah
    }

    fun kurangiStok(jumlah: Int): Boolean {
        return if (cekStok(jumlah)) {
            stok -= jumlah
            true
        } else {
            false
        }
    }

    fun infoBarang() {
        println("Barang: $namaBarang | Harga: Rp${"%.0f".format(harga)} | Stok: $stok")
    }
}

class Kasir(
    val idKasir: String,
    val nama: String
) {
    private val usernameValid = "kasir01"
    private val passwordValid = "itkmart123"

    var isLogin: Boolean = false
        private set

    fun login(username: String, password: String): Boolean {
        return if (username == usernameValid && password == passwordValid) {
            isLogin = true
            println("Login kasir berhasil. Kasir $nama siap memproses transaksi.")
            true
        } else {
            println("Login gagal. Username atau password salah.")
            false
        }
    }

    fun logout() {
        isLogin = false
        println("Kasir logout dari sistem.")
    }

    fun prosesTransaksi(pembeli: Pembeli, barang: Barang, jumlah: Int): Boolean {
        println("\n=== Proses Transaksi ===")
        println("Kasir: $nama")
        println("Pembeli: ${pembeli.nama}")
        println("Barang: ${barang.namaBarang}")
        println("Jumlah beli: $jumlah")

        if (!isLogin) {
            println("Transaksi ditolak: kasir belum login.")
            return false
        }

        if (jumlah <= 0) {
            println("Transaksi ditolak: jumlah pembelian harus lebih dari 0.")
            return false
        }

        if (!barang.cekStok(jumlah)) {
            println("Transaksi gagal: stok barang tidak mencukupi. Stok tersedia: ${barang.stok}.")
            return false
        }

        val totalHarga = barang.harga * jumlah
        println("Total harga: Rp${"%.0f".format(totalHarga)}")

        if (pembeli.uangTunai < totalHarga) {
            println("Transaksi gagal: uang tunai pembeli tidak mencukupi.")
            println("Uang pembeli: Rp${"%.0f".format(pembeli.uangTunai)}")
            return false
        }

        val stokBerhasilDikurangi = barang.kurangiStok(jumlah)
        val pembayaranBerhasil = pembeli.bayar(totalHarga)

        return if (stokBerhasilDikurangi && pembayaranBerhasil) {
            pembeli.tambahPoin(10)
            println("Transaksi berhasil.")
            println("Sisa uang pembeli: Rp${"%.0f".format(pembeli.uangTunai)}")
            println("Sisa stok barang: ${barang.stok}")
            println("Poin member bertambah 10 poin. Total poin: ${pembeli.poinMember}")
            true
        } else {
            println("Transaksi dibatalkan karena terjadi kesalahan proses.")
            false
        }
    }
}

fun main() {
    val kasir = Kasir("K001", "Sinta")
    val pembeliKurangUang = Pembeli("P001", "Anggita", 5000.0)
    val pembeliCukupUang = Pembeli("P002", "Budi", 50000.0)
    val barang = Barang("B001", "Mie Instan", 3500.0, 5)

    println("=== DATA AWAL SISTEM ITK-MART ===")
    barang.infoBarang()
    pembeliKurangUang.infoPembeli()
    pembeliCukupUang.infoPembeli()

    println("\n=== UJI LOGIN ===")
    kasir.login("admin", "salah")
    kasir.login("kasir01", "itkmart123")

    println("\n=== SIMULASI GAGAL 1: UANG TUNAI KURANG ===")
    kasir.prosesTransaksi(pembeliKurangUang, barang, 2)

    println("\n=== SIMULASI GAGAL 2: STOK TIDAK MENCUKUPI ===")
    kasir.prosesTransaksi(pembeliCukupUang, barang, 10)

    println("\n=== SIMULASI SUKSES ===")
    kasir.prosesTransaksi(pembeliCukupUang, barang, 3)

    println("\n=== DATA AKHIR SISTEM ITK-MART ===")
    barang.infoBarang()
    pembeliKurangUang.infoPembeli()
    pembeliCukupUang.infoPembeli()

    kasir.logout()
}