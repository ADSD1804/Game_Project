package game_package;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO = 800;
	private static final int ALTO = 600;

	private static volatile boolean enfuncionamiento = false;

	private static final String NOMBRE = "The_Game";

	private static int aps = 0;
	private static int fps = 0;

	private static JFrame window;
	private static Thread thread;

	private game() {
		setPreferredSize(new Dimension(ANCHO, ALTO));

		window = new JFrame(NOMBRE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setLayout(new BorderLayout());
		window.add(this, BorderLayout.CENTER);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

	}

	public static void main(String[] args) {
		game Game = new game();
		Game.iniciar();
	}

	private synchronized void iniciar() {
		enfuncionamiento = true;

		thread = new Thread(this, "Graficos");
		thread.start();
	}

	private synchronized void detener() {
		enfuncionamiento = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void actualizar() {
		aps++;
	}

	private void mostrar() {
		fps++;
	}

	public void run() {
		final int NS_POR_SEGUNDO = 1000000000;
		final byte APS_OBJETIVO = 60;
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

		long referenciaActualizacion = System.nanoTime();
		long referenciaContador = System.nanoTime();

		double tiempoTranscurrido;
		double delta = 0;

		while (enfuncionamiento) {
			final long inicioBucle = System.nanoTime();

			tiempoTranscurrido = inicioBucle - referenciaActualizacion;
			referenciaActualizacion = inicioBucle;

			delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;
//<>
			while (delta >= 1) {
				actualizar();
				delta--;
			}

			mostrar();

			if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) {
				window.setTitle(NOMBRE + "    APS: " + aps + "   FPS: " + fps);
				aps = 0;
				fps = 0;
				referenciaContador = System.nanoTime();
			}
		}
	}
}
