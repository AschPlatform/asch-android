package asch.so.wallet.event;

/**
 * Created by kimziv on 2018/3/1.
 */

public class DAppDownloadEvent {
    private int status;
    private int soFarBytes;
    private int totalBytes;

    public DAppDownloadEvent(int status, int soFarBytes, int totalBytes) {
        this.status = status;
        this.soFarBytes = soFarBytes;
        this.totalBytes = totalBytes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSoFarBytes() {
        return soFarBytes;
    }

    public void setSoFarBytes(int soFarBytes) {
        this.soFarBytes = soFarBytes;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }
}
