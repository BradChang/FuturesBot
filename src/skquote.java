import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jna.ptr.ShortByReference;

import quote.*;
import quote.FOnNotifyQuote;
import quote.FOnNotifyServerTime;
import skosquote.*;
import skosquote.FOnNotifyConnection;

import java.text.SimpleDateFormat;

public class skquote extends java.lang.Thread
{
	public static int RequestTicks = 1;
	public static int showgui = 1;
	private static final String POSITION_JSON = "capital_futurebot.json";
	static String ca_id = "A123456789", ca_password = "mypassword";
	static String ca_account = "F02000", ca_stock_account = "123456";
	static String symbol = "MTX00";

	public static Queue<String> queue_price = new LinkedList<String>();
	private static NewDdeClient client;
	
	public static Display display = new Display();
	public static Shell shell = new Shell(display);
	public static Label label_TX00 = new Label(shell, SWT.LEFT);
	public static Label label_TWN = new Label(shell, SWT.LEFT);
	public static Label label_KOSPI = new Label(shell, SWT.LEFT);

	public static void main(String[] args)
	{

		int ret;
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		try
		{
			Object obj = parser.parse(new FileReader(POSITION_JSON));
			JSONObject jsonObject = (JSONObject) obj;

			ca_id = (String) jsonObject.get("ca_id");
			System.out.println("ca_id: " + ca_id);

			ca_password = (String) jsonObject.get("ca_password");
			ca_account = (String) jsonObject.get("ca_account");
			System.out.println("ca_account: " + ca_account);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		
		final Shell shell = new Shell(display);
		shell.setSize(600, 200);
		shell.setText("FutureBot群益報價");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		shell.setLayout(gridLayout);
		/*
		final Button getprice = new Button(shell, SWT.PUSH);
		getprice.setText("報價");
		getprice.addSelectionListener(new SelectionListener()
		{

			public void widgetSelected(SelectionEvent event)
			{
				String debug, debugos;
				int ret, retos;
				ShortByReference sbr_1 = new ShortByReference((short) 1);
				ShortByReference sbr_2 = new ShortByReference((short) 2);
				ShortByReference sbr_3 = new ShortByReference((short) 3);
				ShortByReference sbr_4 = new ShortByReference((short) 4);
				if (RequestTicks == 1)
				{
					ret = SKQuoteLib.INSTANCE.SKQuoteLib_RequestTicks(sbr_1, "TX00");
					debug = "RequestTicks = ";
					retos = SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_1, "SGX,TWN1501");
					retos += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_2, "17,KOSPI");
					retos += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_3, "NYM,GC1502");
					retos += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_4, "NYM,CL1502");
					debugos = "OS_RequestTicks = ";
				}
				else
				{
					ret = SKQuoteLib.INSTANCE.SKQuoteLib_RequestStocks(sbr_1, "TX00");
					debug = "RequestStocks = ";
					retos = SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestStocks(sbr_1,
							"SGX,TWN1501#17,KOSPI#NYM,GC1502#NYM,CL1502");
					debugos = "OS_RequestStocks = ";

				}
				debug += ret;
				System.out.println(debug);
				debugos += retos;
				System.out.println(debugos);
			}

			public void widgetDefaultSelected(SelectionEvent event)
			{}

		});
*/
		// System.out.println("start FutureBot");
		/*
		 * final Button button = new Button(shell, SWT.PUSH); button.setText("接收報價"); final Button close = new Button(shell, SWT.PUSH); close.setText("結束連線");
		 */
		Point p;
		final Label label_TX00 = new Label(shell, SWT.LEFT);
		final Label label_TWN = new Label(shell, SWT.LEFT);
		final Label label_KOSPI = new Label(shell, SWT.LEFT);
		label_TX00.setText("TX00,9108.0,0,9109.0,0,9109.0,9229.0,9082.0,4,78465,9194.0sd;lfkgjsdl;kg");
		// p = label_TX00.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		// label_TX00.setBounds(100, 0, p.x + 5, p.y + 5);

		label_TWN.setText("TX00,9108.0,0,9109.0,0,9109.0,9229.0,9082.0,4,78465,9194.0");
		// p = label_TWN.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		// label_TWN.setBounds(200, 0, p.x + 5, p.y + 5);

		label_KOSPI.setLocation(0, 300);

		client = new NewDdeClient();

		skinit init = new skinit();
		init.start();

		try
		{
			Thread.sleep(6000);
		}
		catch (InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
		//System.out.println("skinit alive " + init.isAlive());
		ShortByReference sbr_1 = new ShortByReference((short) 1);
		ShortByReference sbr_2 = new ShortByReference((short) 2);
		ShortByReference sbr_3 = new ShortByReference((short) 3);
		ShortByReference sbr_4 = new ShortByReference((short) 4);
		ret = SKQuoteLib.INSTANCE.SKQuoteLib_RequestTicks(sbr_1, "TX00");
		System.out.println("RequestTicks = " + ret);
		int retos = SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_1, "SGX,TWN1501");
		retos += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_2, "17,KOSPI");
		retos += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_3, "NYM,GC1502");
		retos += SKOSQuoteLib.INSTANCE.SKOSQuoteLib_RequestTicks(sbr_4, "NYM,CL1502");
		System.out.println("OS_RequestTicks = " + retos);

		/*
		 * 
		 * String debug;
		 * 
		 * String tick = "TX00"; if (RequestTicks == 1) { ret = SKQuoteLib.INSTANCE.SKQuoteLib_RequestTicks(sbr_neg, tick); debug = "SKQuoteLib_RequestTicks = "; } else { ret = SKQuoteLib.INSTANCE.SKQuoteLib_RequestStocks(sbr_neg, "TX00"); debug = "RequestStocks = "; } debug += ret; System.out.println(debug);
		 */

		if (showgui == 1)
		{
			shell.open();
			while (!shell.isDisposed())
			{
				if (!display.readAndDispatch())
					display.sleep();
				if (RequestTicks == 1)
				{
					label_TX00.setText(TicksGet.msg);
					label_TWN.setText(TicksGetOS.msg);
				}
				else
				{
					label_TX00.setText(quote.FOnNotifyQuote.msg);
					label_TWN.setText(TicksGetOS.msg);
				}
				while(!queue_price.isEmpty())
				{
					 client.doit(queue_price.remove());
				}
			}
			display.dispose();
		}
		else
		{
			// NewDdeClient client = new NewDdeClient();
			boolean OutServer = false;
			while (!OutServer)
			{
				client.runflag = true;
				while (!queue_price.isEmpty())
					client.doit(queue_price.remove());

				OutServer = check_runtime();
			}
			client.runflag = false;
			client.close();
		}
		// client.close();
	}// main

	static boolean check_runtime()
	{
		java.util.Date now = new java.util.Date(); // 取得現在時間.
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss E", java.util.Locale.TAIWAN);
		String sGMT = sf.format(now);
		int hour = Integer.valueOf(sGMT.substring(0, 2)).intValue();
		int min = Integer.valueOf(sGMT.substring(3, 5)).intValue();
		int sec = Integer.valueOf(sGMT.substring(6, 8)).intValue();
		if (hour > 12 && min > 49 && sec > 0)
		{
			return true;
		}
		else
			return false;
	}
}
