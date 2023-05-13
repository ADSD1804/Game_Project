package game_package;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

import control.Teclado;
import graphics_game.Pantalla;

public class game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final int ANCHO = 800;
	private static final int ALTO = 600;

	private static volatile boolean enfuncionamiento = false;

	private static final String NOMBRE = "The_Game";

	private static int aps = 0;
	private static int fps = 0;

	private static int x = 0;
	private static int y = 0;
	
	private static JFrame window;
	private static Thread thread;
	private static Teclado teclado;
	private static Pantalla pantalla;
	
	private static BufferedImage imagen = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
	private static int[] pixeles = ((DataBufferInt) imagen.getRaster().getDataBuffer()).getData();

	private game() {
		setPreferredSize(new Dimension(ANCHO, ALTO));
		
		teclado = new Teclado();
		addKeyListener(teclado);
		
		pantalla = new Pantalla(ANCHO, ALTO);

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

	@SuppressWarnings("unused")
	private synchronized void detener() {
		enfuncionamiento = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void actualizar() {
		teclado.actualizar();
		
		if (teclado.arriba) {
			y++;
		}
		if (teclado.abajo) {
			y--;
		}
		if (teclado.derecha) {
			x++;
		}
		if (teclado.izquierda) {
			x--;
		}
		
		
		aps++;
		
	}

	private void mostrar() {
		BufferStrategy estrategia = getBufferStrategy();
		
		if (estrategia == null) {
			createBufferStrategy(3);
			return;
		}
		
		pantalla.limpiar();
		pantalla.mostrar(x, y);
		
		System.arraycopy(pantalla.pixeles, 0, pixeles, 0, pixeles.length);
		
		Graphics g = estrategia.getDrawGraphics();
		
		g.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		
		estrategia.show();
		
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
		
		requestFocus();

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
