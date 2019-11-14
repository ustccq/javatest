package ustccq.test;

import java.math.BigDecimal;

import com.udojava.evalex.Expression;

public class EvalExpression {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Expression notEqual = new Expression("3!=3");
		System.err.println(notEqual.eval());
		Expression preExpression = new Expression("(1568864608-1568864605)<(1568864609-1568864605)");
		preExpression.setPrecision(20);
		System.out.println(preExpression.eval());
		BigDecimal result = new Expression("1568864608<1568864609").setPrecision(100).eval();
		if (BigDecimal.ONE == result)
			System.out.println("OK");
		else
			System.out.println("result is:" + result);

		System.out.println(new Expression("1568864608-1568864605").eval());
		System.out.println(new Expression("568864608-568864605").eval());
		System.out.println(new Expression("68864608-68864605").eval());
		System.out.println(new Expression("8864608-8864605").eval());
		System.out.println(new Expression("864608-864605").eval());
		System.out.println(new Expression("64608-64605").eval());
		System.out.println(new Expression("4608-4605").eval());
		System.out.println(new Expression("608-605").eval());
		System.out.println(new Expression("08-05").eval());
		System.out.println(new Expression("8-5").eval());
	}
}
