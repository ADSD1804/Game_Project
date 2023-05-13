package graphics_game;

public final class Sprite {
	
	private final int tamano;
	
	private int x;
	private int y;
	
	public int[] pixeles;
	private final HojaSprites hoja;
	
	//Coleccion de sprites
	
	public static Sprite cesped = new Sprite(32, 0, 0, HojaSprites.hierba);
	
	//Fin coleccion
	
	public Sprite(final int tamano, final int columna, final int fila, final HojaSprites hoja) {
		this.tamano = tamano;
		
		pixeles = new int[tamano * tamano];
		
		this.x = columna * tamano;
		this.y = fila * tamano;
		this.hoja = hoja;
		
		for (int y = 0; y < tamano; y++) {
			for (int x = 0; x < tamano; x++) {
				pixeles[(x + y) * tamano] = hoja.pixeles[(x + this.x) + (y + this.y) * hoja.getAncho()];
			}
		}
	}

	public HojaSprites getHoja() {
		return hoja;
	}

	public int getTamano() {
		return tamano;
	}
	
}
