package uz.pdp.springsecurity.payload.statistics;

public interface AttendanceStat {
    Integer getLateDaysCount();
    Double getTotalLateMinutes();
    Integer getEarlyLeaveDaysCount();
    Double getTotalEarlyLeaveMinutes();
    Double getTotalWorkHours();
}
