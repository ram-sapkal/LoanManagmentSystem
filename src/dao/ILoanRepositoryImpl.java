package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.CarLoan;
import entity.HomeLoan;
import entity.Loan;
import exception.InvalidLoanException;
import util.DBConnection;

public class ILoanRepositoryImpl implements ILoanRepository {
	
	public static Loan loan=null;
    Connection connection;
	
	public ILoanRepositoryImpl(){
		try {
			connection=DBConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void applyLoan(Loan obj ) throws SQLException{
		
		
		String query = "INSERT INTO Loan (loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ps1 = connection.prepareStatement(query);
		//set values to query
		ps1.setInt(1, obj.getLoanId());
		ps1.setInt(2, obj.getCustomerId());
		ps1.setDouble(3, obj.getPrincipalAmount());
		ps1.setDouble(4, obj.getInterestRate());
		ps1.setInt(5, obj.getLoanTerm());
		ps1.setString(6, obj.getLoanType());
		ps1.setString(7, obj.getLoanStatus());
		
		ps1.executeUpdate();
		
		if(obj.getLoanType().equals("CarLoan")) {
			String query1 = "INSERT INTO CarLoan(loanId,carModel,carValue) values(?,?,?)";
					
			PreparedStatement ps2 = connection.prepareStatement(query1);
			ps2.setInt(1, obj.getLoanId());
			ps2.setString(2, ((CarLoan) obj).getCarModel());
			ps2.setInt(3, ((CarLoan) obj).getCarValue());
			ps2.executeUpdate();
		}
		else if(obj.getLoanType().equals("HomeLoan")) {
			String query1 = "INSERT INTO homeloan(loanId,propertyAddress,propertyValue) values(?,?,?)";
			
			PreparedStatement ps2 = connection.prepareStatement(query1);
			ps2.setInt(1, obj.getLoanId());
			ps2.setString(2, ((HomeLoan) obj).getPropertyAddress());
			ps2.setInt(3, ((HomeLoan) obj).getPropertyValue());
			ps2.executeUpdate();
		}
				
	}	
	
	public  void getAllLoan() throws SQLException  {
		Statement stmt =  connection.createStatement();
		String query = "SELECT * FROM loan";
		ResultSet resultSet = stmt.executeQuery(query);
		System.out.println("loanId || customerId || principalAmount || interestRate || loanTerm || loanType || loanStatus");
		while(resultSet.next()) 
		{
			int loanId=resultSet.getInt("loanId");
			int customerId=resultSet.getInt("customerId");
			Double principalAmount=resultSet.getDouble("principalAmount");
			Double interestRate=resultSet.getDouble("interestRate");
			int loanTerm=resultSet.getInt("loanTerm");
			String loanType=resultSet.getString("loanType");
			String loanStatus=resultSet.getString("loanStatus");
			
			System.out.println(" "+loanId+" || " +customerId+" ||"+" || "+principalAmount+" || "+interestRate+" || "+loanTerm+" || "+loanType+" || "+loanStatus);
		}	
		
	}
	
	public static Loan getLoanDetails(int loanId) throws InvalidLoanException,Exception {
		try {
		String query = "SELECT * FROM loan WHERE loanId=?";
		PreparedStatement ps1 = DBConnection.connection.prepareStatement(query);
		
		ps1.setInt(1, loanId);
		
		ResultSet resultSet = ps1.executeQuery();
		if(resultSet.next()) {
		loan=new Loan(resultSet.getInt("loanId"),
				resultSet.getInt("customerId"),
				resultSet.getDouble("principalAmount"),
				resultSet.getDouble("interestRate")
				,resultSet.getInt("loanTerm"),
				resultSet.getString("loanType"),
				resultSet.getString("loanStatus"));
		}else {
			 throw new InvalidLoanException("Loan is not Generated.");
		}
		}catch(Exception e) {
			System.out.println("Exception is :- "+e.getMessage());
		}
		return loan;
	}
	
	public void  getLoanById(int loanId) throws Exception{
		
		Loan loanDetail = getLoanDetails(loanId);
		if(loanDetail!=null) {
		System.out.println(loanDetail.toString());
		}
	}
	
	public void calculateInterest(int loanId) throws Exception{
		Loan loanDetail = getLoanDetails(loanId);
		if(loanDetail!=null) {
	    Double principalAmount=loanDetail.getPrincipalAmount();
	    Double interestRate=loanDetail.getInterestRate();
	    int loanTenure=loanDetail.getLoanTerm();
        System.out.println(principalAmount+"---"+interestRate+"----"+loanTenure);
	    Double interest=(principalAmount * interestRate * loanTenure)/12;
	    System.out.println("interest :" +interest);
		}
	}
	
	public int calculateEMI(int loanId) throws Exception{
		Loan loanDetail = getLoanDetails(loanId);
		int Emi=0;
		if(loanDetail!=null) {
		Double R=((loanDetail.getInterestRate())/12/100);
		Double P=loanDetail.getPrincipalAmount();
		int N=loanDetail.getLoanTerm();
		Emi=(int)((P * R * Math.pow(1+R,N)) / (Math.pow(1+R,N)-1));
		System.out.println("EMI :-"+Emi);
		}
		return Emi;
	}
	
	public void loanRepayment(int loanId,int amount) throws Exception,InvalidLoanException{
		try {
			int emiAmount=calculateEMI(loanId);
			int numberOfEmis=(int)(amount/emiAmount);
			if(amount < emiAmount || numberOfEmis <=0) {
				throw new InvalidLoanException("Payment is rejected");
			}else {
				System.out.println("Payment successful and number of emi's paid : "+numberOfEmis);
			}
		}catch(Exception e) {
			System.out.println("Exception Occured : "+e.getMessage());
		}
	}
	
	public void loanStatus(int loanId) throws InvalidLoanException{
		try {
			
			String query = "SELECT creditScore FROM Customer c JOIN Loan l ON c.customerID = l.customerID WHERE l.loanID = ?";
			PreparedStatement ps1 = connection.prepareStatement(query);
			ps1.setInt(1,loanId);
			ResultSet lstatus  = ps1.executeQuery();
			int creditScore;
			String ls="Pending";
			if (lstatus.next()) {
                 creditScore= lstatus.getInt("creditScore");
                 if (creditScore > 650) {
                	 ls="Approved";
                	 System.out.println("Loan with ID " + loanId + " is approved.");
                 }
                 else { 
                	 ls = "Rejected";
                	 System.out.println("Loan with ID " + loanId + " is rejected.");
                 }
 
            }
			
			String updateLoanStatusQuery = "UPDATE Loan SET loanStatus = ? WHERE loanId = ?";
            PreparedStatement ps2= connection.prepareStatement(updateLoanStatusQuery);
            ps2.setString(1, ls);
            ps2.setInt(2, loanId);
            int rowsAffected =ps2.executeUpdate();

              if (rowsAffected > 0) {
                  System.out.println("Loan Status Updated: " + ls);
              } else {
                  throw new InvalidLoanException("Failed to update loan status.");
              }
			
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
}
