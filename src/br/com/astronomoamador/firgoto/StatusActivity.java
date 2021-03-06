package br.com.astronomoamador.firgoto;

import java.io.IOException;
import br.com.astronomoamador.firgoto.bluetooth.ConnectedThread;
import br.com.astronomoamador.firgoto.bluetooth.Constants;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CheckBox;

public class StatusActivity extends Activity {
	private int UTC = 0;
	private BluetoothDevice mmDevice;
	private ConnectedThread connectedThread;
	private ToggleButton toggleNorteSulLat;
	private EditText editTextGrauLat;
	private EditText editTextMinLat;
	private CheckBox CheckLat;
	private ToggleButton toggleLestOestLog;
	private EditText editTextGrauLog;
	private EditText editTextMinLog;
	private CheckBox CheckLog;
	private EditText editTextHoraTime;
	private EditText editTextMinTime;
	private EditText editTextSegTime;
	private CheckBox CheckTime;
	private ToggleButton toggleUTC;
	private EditText editTextUTCSet;
	private CheckBox CheckUTC;
	private TextView TextHoraUTC;
	private TextView TextMinUTC;
	private TextView TextSegUTC;
	private TextView TextHoraLST;
	private TextView TextMinLST;
	private TextView TextSegLST;
	private EditText editTextDia;
	private EditText editTextMes;
	private EditText editTextAno;
	private CheckBox CheckData;
	private String bufferCmd = "";
	private String response[] = { "#", "#", "#", "#", "#", "#", "#", "#", "#" };
	private String command[] = { ":Gt#", ":Gt#", ":GG#", ":GL#", ":GS#",
			":GC#", ":Gg#", ":Gt#" };
	private boolean podeescrever = true;
	private String commandAtual = null;
	private int icom = 0;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				if (readMessage.contains(response[icom])
						|| response[icom].contains(readMessage)) {
					podeescrever = false;
					bufferCmd = bufferCmd + readMessage;
					leResposta(bufferCmd);
					bufferCmd = "         ";
					bufferCmd = "";
					podeescrever = true;

				} else {
					bufferCmd = bufferCmd + readMessage;
				}
				readBuf = null;
				readMessage = null;
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		toggleNorteSulLat = (ToggleButton) findViewById(R.id.toggleButtonLat);
		editTextGrauLat = (EditText) findViewById(R.id.editTextGrauLat);
		editTextMinLat = (EditText) findViewById(R.id.editTextMinLat);
		CheckLat = (CheckBox) findViewById(R.id.checkBoxLat);
		toggleLestOestLog = (ToggleButton) findViewById(R.id.ToggleButtonLog);
		editTextGrauLog = (EditText) findViewById(R.id.editTextGrauLog);
		editTextMinLog = (EditText) findViewById(R.id.editTextMinLog);
		CheckLog = (CheckBox) findViewById(R.id.CheckBoxLog);
		editTextHoraTime = (EditText) findViewById(R.id.editTextHoraTime);
		editTextMinTime = (EditText) findViewById(R.id.editTextMinTime);
		editTextSegTime = (EditText) findViewById(R.id.editTextSegTime);
		CheckTime = (CheckBox) findViewById(R.id.CheckBoxTime);
		toggleUTC = (ToggleButton) findViewById(R.id.ToggleButtonUTC);
		editTextUTCSet = (EditText) findViewById(R.id.editTextUTCSet);
		CheckUTC = (CheckBox) findViewById(R.id.CheckBoxUTC);
		TextHoraUTC = (TextView) findViewById(R.id.TextHoraUTC);
		TextMinUTC = (TextView) findViewById(R.id.TextMinUTC);
		TextSegUTC = (TextView) findViewById(R.id.TextSegUTC);
		TextHoraLST = (TextView) findViewById(R.id.TextHoraLST);
		TextMinLST = (TextView) findViewById(R.id.TextMinLST);
		TextSegLST = (TextView) findViewById(R.id.TextSegLST);
		editTextDia = (EditText) findViewById(R.id.editTextDia);
		editTextMes = (EditText) findViewById(R.id.editTextMes);
		editTextAno = (EditText) findViewById(R.id.editTextAno);
		CheckData = (CheckBox) findViewById(R.id.CheckBoxData);
		mmDevice = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if (mmDevice != null) {
			try {
				connectedThread = new ConnectedThread(mmDevice, mHandler);
				new Thread(connectedThread).start();
				MandaComando();
			} catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				// e.printStackTrace();
			}
		}
	}

	private void MandaComando() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!connectedThread.isFinished()) {
					if (podeescrever) {
						try {
							Thread.sleep(300);
							connectedThread.write(command[icom].getBytes());
							commandAtual = command[icom];

							if (icom == 0) {
								command[0] = "";
							}
							if (icom == 1) {
								command[1] = "";
							}
							if (icom == 2) {
								command[2] = "";
							}
							if (icom == 3) {
								command[3] = "";
							}
							if (icom == 4) {
								command[4] = "";
							}
							if (icom == 5) {
								command[5] = "";
							}
							if (icom == 6) {
								command[6] = "";
							}
							if (icom == 7) {
								command[7] = "";
							}
							icom++;
							if (icom > 7) {
								icom = 0;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (mmDevice != null) {
				Intent result = new Intent();
				result.putExtra(BluetoothDevice.EXTRA_DEVICE, mmDevice);
				setResult(RESULT_OK, result);
				connectedThread.finish();
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void leResposta(String readMessage) {
		String strtemp;
		try {
			if (":Gt#".equals(commandAtual)) {
				strtemp = readMessage.subSequence(0, 1).toString();
				if (strtemp.equalsIgnoreCase("-")) {
					toggleLestOestLog.setChecked(true);
				} else {
					toggleLestOestLog.setChecked(false);
				}
				editTextGrauLat.setText(readMessage.subSequence(1, 3));
				editTextMinLat.setText(readMessage.subSequence(4, 6));
			}
			if (":Gg#".equals(commandAtual)) {
				strtemp = readMessage.subSequence(0, 1).toString();
				if (strtemp.equalsIgnoreCase("-")) {
					toggleNorteSulLat.setChecked(true);
				} else {
					toggleNorteSulLat.setChecked(false);
				}
				editTextGrauLog.setText(readMessage.subSequence(1, 4));
				editTextMinLog.setText(readMessage.subSequence(5, 7));
			}
			if (":GL#".equals(commandAtual)) {;
				editTextHoraTime.setText(readMessage.subSequence(0, 2));
				editTextMinTime.setText(readMessage.subSequence(3, 5));
				editTextSegTime.setText(readMessage.subSequence(6, 8));
				TextHoraUTC.setText(readMessage.subSequence(0, 2));
				TextMinUTC.setText(readMessage.subSequence(3, 5));
				TextSegUTC.setText(readMessage.subSequence(6, 8));
			}
			if (":GG#".equals(commandAtual)) {
				editTextUTCSet.setText(String.valueOf(readMessage.subSequence(
						1, 3)));
				strtemp = readMessage.subSequence(0, 1).toString();
				if (strtemp.equalsIgnoreCase("-")) {
					toggleUTC.setChecked(true);
					strtemp = readMessage.subSequence(1, 3).toString();
					UTC = (Integer.parseInt(strtemp)) * -1;
				} else {
					toggleUTC.setChecked(false);
					strtemp = readMessage.subSequence(1, 3).toString();
					UTC = Integer.parseInt(strtemp);
				}

			}

			if (":GS#".equals(commandAtual)) {
				TextHoraLST.setText(readMessage.subSequence(0, 2));
				TextMinLST.setText(readMessage.subSequence(3, 5));
				TextSegLST.setText(readMessage.subSequence(6, 8));
			}
			if (":GC#".equals(commandAtual)) {
				editTextMes.setText(readMessage.subSequence(0, 2));
				editTextDia.setText(readMessage.subSequence(3, 5));
				editTextAno.setText(readMessage.subSequence(6, 8));
			}
			if (commandAtual.contains(":St") || commandAtual.contains(":Sg")
					|| commandAtual.contains(":SL")
					|| commandAtual.contains(":SG")
					|| commandAtual.contains(":SC"))
				if (readMessage.equalsIgnoreCase("1")) {
					Toast.makeText(getApplicationContext(),
							R.string.atualizado, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.deu_erro,
							Toast.LENGTH_LONG).show();
				}

		} catch (Exception e) {
			strtemp = "error  " + commandAtual + " - " + readMessage.toString();
			Toast.makeText(getApplicationContext(), strtemp, Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}

	}

	public void atualizar(View v) {
		String strtmp;
		int i;
		if (CheckLat.isChecked()) {
			if (!toggleNorteSulLat.isChecked()) {
				strtmp = ":St+";
			} else {
				strtmp = ":St-";
			}
			i = Integer.parseInt(editTextGrauLat.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "*";
			i = Integer.parseInt(editTextMinLat.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[6] = strtmp;
			response[6] = "0123456789";
			CheckLat.setChecked(false);
		} else {
			command[6] = ":Gt#";
			response[6] = "#";
			command[7] = ":Gt#";
			response[7] = "#";
		}
		if (CheckLog.isChecked()) {
			// :SgDDD*MM#
			if (!toggleLestOestLog.isChecked()) {
				strtmp = ":Sg+";
			} else {
				strtmp = ":Sg-";
			}
			i = Integer.parseInt(editTextGrauLog.getText().toString());
			strtmp = strtmp + String.format("%03d", i) + "*";
			i = Integer.parseInt(editTextMinLog.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[1] = strtmp;
			response[1] = "0123456789";
			CheckLog.setChecked(false);
		} else {
			command[1] = ":Gg#";
			response[1] = "#";
		}
		/*
		 * :SLHH:MM:SS# Set the local Time Returns: 0 � Invalid
		 */
		if (CheckTime.isChecked()) {
			// :SLHH:MM:SS#
			strtmp = ":SL";
			i = Integer.parseInt(editTextHoraTime.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + ":";
			i = Integer.parseInt(editTextMinTime.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + ":";
			i = Integer.parseInt(editTextSegTime.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[3] = strtmp;
			response[3] = "0123456789";
			CheckTime.setChecked(false);
		} else {
			command[3] = ":GL#";
			response[3] = "#";
		}
		/*
		 * :SGsHH.H# Set the number of hours added to local time to yield UTC
		 * Returns: 0 � Invalid 1 - Valid
		 */
		if (CheckUTC.isChecked()) {
			// :SGsHH#
			if (!toggleUTC.isChecked()) {
				strtmp = ":SG+";
			} else {
				strtmp = ":SG-";
			}
			i = Integer.parseInt(editTextUTCSet.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[2] = strtmp;
			response[2] = "0123456789";
			CheckUTC.setChecked(false);
		} else {
			command[2] = ":GG#";
			response[2] = "#";
		}
		command[4] = ":GS#";
		response[4] = "#";
		/*
		 * :SCMM/DD/YY# Change Handbox Date to MM/DD/YY Returns: <D><string> D =
		 * �0� if the date is invalid. The string is the null string. D = �1�
		 * for valid dates and the string is �Updating Planetary Data# #� Note:
		 * For LX200GPS this is the UTC data!
		 */
		if (CheckData.isChecked()) {
			// ::SCMM/DD/YY#
			strtmp = ":SC";
			i = Integer.parseInt(editTextMes.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "/";
			i = Integer.parseInt(editTextDia.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "/";
			i = Integer.parseInt(editTextAno.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[5] = strtmp;
			response[5] = "0123456789";
			CheckData.setChecked(false);
		} else {
			command[5] = ":GC#";
			response[5] = "#";
		}
		icom = 0;
	}

}
