package know_wave.comma.account.entity;

public enum AcademicStatus {
    Enrolled("재학"), OnLeave("휴학"), Graduate("졸업");

    private final String status;

    AcademicStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
