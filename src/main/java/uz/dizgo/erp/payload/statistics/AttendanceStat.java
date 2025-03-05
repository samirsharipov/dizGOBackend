package uz.dizgo.erp.payload.statistics;

public interface AttendanceStat {
    Integer getLateDaysCount();
    Double getTotalLateMinutes();
    Integer getEarlyLeaveDaysCount();
    Double getTotalEarlyLeaveMinutes();
    Double getTotalWorkHours();
}
