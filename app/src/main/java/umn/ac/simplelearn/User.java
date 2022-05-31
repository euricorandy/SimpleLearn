package umn.ac.simplelearn;

public class User {

    private String email, namaMahasiswa, nim, pass;
    private long coins = 25;

    public User() {
    }

    public User(String email, String namaMahasiswa, String pass, String nim) {
        this.email = email;
        this.namaMahasiswa = namaMahasiswa;
        this.pass = pass;
        this.nim = nim;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }

    public void setNamaMahasiswa(String namaMahasiswa) {
        this.namaMahasiswa = namaMahasiswa;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }
}
