package mainMod;

import java.sql.Connection;
import java.util.Scanner;

import dao.ILoanRepositoryImpl;
import entity.CarLoan;
import entity.HomeLoan;
import entity.Loan;
import exception.InvalidLoanException;
import util.DBConnUtil;
import util.DBConnection;

public class LoanManagment {
	public static void main(String[] args) throws Exception,InvalidLoanException{
		
		while(true) {
			System.out.println();
			System.out.println("--x-- Loan Managment Application --x--");
			System.out.println("applyLoan : 1");
			System.out.println("getAllLoan : 2");
			System.out.println("getLoan : 3");
			System.out.println("loanRepayment : 4");
			System.out.println("Exit : 5");
			System.out.print("Select an option :");
			Scanner sc = new Scanner(System.in);
			int cv = sc.nextInt();
			
			if(cv>=1 && cv<=5) {
			
			if(cv==1) {
				System.out.println();
				System.out.println("Car Loan : 1");
				System.out.println("Home Loan : 2");
				System.out.print("Select option : ");
				System.out.println();
				int lc=sc.nextInt();
				Loan obj;
				if(lc==1) {
					System.out.println();
					System.out.println("For Car Loan enter the following details");
					System.out.println();
					System.out.print("Enter LoanID : ");
					int loanId = sc.nextInt();
					System.out.print("Enter CustomerID : ");
					int customerId = sc.nextInt();
					System.out.print("Enter Principle Amount : ");
					double principalAmount = sc.nextDouble();
					System.out.print("Enter interest Rate : ");
					double interestRate = sc.nextDouble();
					System.out.print("Enter loan Term :");
					int loanTerm = sc.nextInt();
					//System.out.print("Enter Loan Type :");
					String loanType = "CarLoan";
					System.out.print("Enter Loan Status : ");
					String loanStatus = sc.next();
					sc.nextLine();
					System.out.print("Enter Car Model : ");
					String carModel = sc.nextLine();
					System.out.print("Enter car Value : ");
					int carValue = sc.nextInt();
					
					 obj= new CarLoan(loanId, customerId, principalAmount,interestRate, loanTerm, loanType, loanStatus, carModel, carValue) ; 
					
				}else if(lc==2) {
					System.out.println();
					System.out.println("For Car Loan enter the following details");
					System.out.println();
					System.out.print("Enter LoanID : ");
					int loanId = sc.nextInt();
					System.out.print("Enter CustomerID : ");
					int customerId = sc.nextInt();
					System.out.print("Enter Principle Amount : ");
					double principalAmount = sc.nextDouble();
					System.out.print("Enter interest Rate : ");
					double interestRate = sc.nextDouble();
					System.out.print("Enter loan Term :");
					int loanTerm = sc.nextInt();
					String loanType = "HomeLoan";
					System.out.print("Enter Loan Status : ");
					String loanStatus = sc.next();
					System.out.print("Enter Property Value : ");
					int propertyValue=sc.nextInt();
					sc.nextLine();// consumes the extra buffer characters
					System.out.print("Enter Property Address : ");
					String propertyAddress=sc.nextLine();
					obj=new HomeLoan(loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus, propertyAddress, propertyValue);
					
				}
				else {
					System.out.println("wrong choice");
					continue;
				}
				ILoanRepositoryImpl loanRepository = new ILoanRepositoryImpl();
				loanRepository.applyLoan(obj);
			}
			else if(cv==2) {
				System.out.println();
				System.out.println("All Loans");
				ILoanRepositoryImpl loanRepository = new ILoanRepositoryImpl();
				loanRepository.getAllLoan();
			}
			else if(cv==3) {
				System.out.println();
				System.out.println("Get Loan : ");
				System.out.print("Enter Loan ID : ");
				int loanId = sc.nextInt();
				ILoanRepositoryImpl loanRepository = new ILoanRepositoryImpl();
				loanRepository.getLoanById(loanId);
			}
			else if(cv==4) {
				System.out.println();
				System.out.println("Loan RePayment");
				System.out.print("Enter Loan ID :");
				int loanId = sc.nextInt();
				System.out.print("Enter Amount :");
				int amount = sc.nextInt();
				ILoanRepositoryImpl loanRepository = new ILoanRepositoryImpl();
				loanRepository.loanRepayment(loanId,amount);
			}else if(cv==5) {
				System.out.println();
				System.out.println("System close");
				System.exit(0);
			}
			else {
				DBConnection.connection.close();
				break;
			}
		}else {
			System.out.println("Wrong option try again!");
			continue;
		}
		
		}
	}
}
