package asch.so.wallet.event;

/**
 * Created by kimziv on 2018/3/1.
 */

public class DAppDownloadEvent {
    private int downloadId;
    private int status;
    private long soFarBytes;
    private long totalBytes;

    public DAppDownloadEvent(int downloadId, int status, long soFarBytes, long totalBytes) {
        this.downloadId=downloadId;
        this.status = status;
        this.soFarBytes = soFarBytes;
        this.totalBytes = totalBytes;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSoFarBytes() {
        return soFarBytes;
    }

    public void setSoFarBytes(long soFarBytes) {
        this.soFarBytes = soFarBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }
}
