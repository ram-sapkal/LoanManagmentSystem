package dao;

import entity.Loan;

public interface ILoanRepository {

	void  applyLoan(Loan obj) throws Exception;
	void  getAllLoan() throws Exception ;
	void  getLoanById(int loanId) throws Exception;
	
}
