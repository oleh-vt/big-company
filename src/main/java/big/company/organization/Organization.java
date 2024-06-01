package big.company.organization;

import big.company.organization.impl.BigCompanyOrganization;

import java.util.List;

public interface Organization {
    List<BigCompanyOrganization.SalaryAnalysisResult> analyzeManagerSalaries();

    List<BigCompanyOrganization.StructureAnalysisReport> analyzeStructure();
}
