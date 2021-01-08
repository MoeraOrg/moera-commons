package org.moera.naming.rpc;

public class RegisteredNameInfo {

    private String name;
    private int generation;
    @Deprecated
    private boolean latest;
    private byte[] updatingKey;
    private String nodeUri;
    @Deprecated
    private long deadline;
    private byte[] signingKey;
    private Long validFrom;
    private byte[] digest;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    @Deprecated
    public boolean isLatest() {
        return latest;
    }

    @Deprecated
    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public byte[] getUpdatingKey() {
        return updatingKey;
    }

    public void setUpdatingKey(byte[] updatingKey) {
        this.updatingKey = updatingKey;
    }

    public String getNodeUri() {
        return nodeUri;
    }

    public void setNodeUri(String nodeUri) {
        this.nodeUri = nodeUri;
    }

    @Deprecated
    public long getDeadline() {
        return deadline;
    }

    @Deprecated
    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public byte[] getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(byte[] signingKey) {
        this.signingKey = signingKey;
    }

    public Long getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Long validFrom) {
        this.validFrom = validFrom;
    }

    public byte[] getDigest() {
        return digest;
    }

    public void setDigest(byte[] digest) {
        this.digest = digest;
    }

}
