package pl.edu.agh.cs.to.cinemamak.model;

public enum RoleName {
    Admin, Manager, Employee;

    public static RoleName getEnum(String roleNameString){
        switch(roleNameString){
            case "Admin" -> { return RoleName.Admin; }
            case "Manager" -> { return RoleName.Manager; }
            case "Employee" -> { return RoleName.Employee; }
            default -> { throw new IllegalArgumentException(); }
        }
    }

}
