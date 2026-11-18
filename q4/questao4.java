import java.io.File;
import java.util.Scanner;
class Data{

	private int ano;
	private int mes;
	private int dia;

	public Data(int ano, int mes, int dia){
		this.ano = ano;
		this.mes = mes;
		this.dia = dia;
	}

	public int getAno(){ return ano; }
	public int getMes() { return mes; }
	public int getDia() { return dia; }

	public String format(){
		return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano);
	}

	public static Data parseData(String s){
		String[] partes = s.split("-");
		int ano = Integer.parseInt(partes[0]);
		int mes = Integer.parseInt(partes[1]);
		int dia = Integer.parseInt(partes[2]);

		return new Data(ano, mes, dia);
	}

}

class Veiculo{
	private int id;
	private String marca;
	private String modelo;
	private int ano;
	private String categoria;
	private String[] combustivel;
	private int cilindros;
	private double cilindrada;
	private String transmissao;
	private String tracao;
	private double consumoCidade;
	private double consumoEstrada;
	private double co2;
	private boolean turbo;
	private Data dataRegistro;

	public Veiculo(int id, String marca, String modelo, int ano, String categoria,
			String[] combustivel, int cilindros, double cilindrada, String transmissao,
			String tracao, double consumoCidade, double consumoEstrada, double co2,
			boolean turbo, Data dataRegistro){
		this.id = id;
		this.marca = marca;
		this.modelo = modelo;
		this.ano = ano;
		this.categoria = categoria;
		this.combustivel = combustivel;
		this.cilindros = cilindros;
		this.cilindrada = cilindrada;
		this.transmissao = transmissao;
		this.tracao = tracao;
		this.consumoCidade = consumoCidade;
		this.consumoEstrada = consumoEstrada;
		this.co2 = co2;
		this.turbo = turbo;
		this.dataRegistro = dataRegistro;
	}

	public int getId() { return id;}
	public String getMarca() { return marca; }
	public String getModelo() { return modelo; }
	public int getAno(){ return ano; }
	public String getCategoria() { return categoria;}
	public String[] getCombustivel() { return combustivel; }
	public int getCilindros() { return cilindros; }
	public double getCilindrada() { return cilindrada; }
	public String getTransmissao() { return transmissao; }
	public String getTracao() { return tracao; }
	public double getConsumoCidade() { return consumoCidade; }
	public double getConsumoEstrada() {return consumoEstrada;}
	public double getCo2() { return co2; }
	public boolean getTurbo() {return turbo;}
	public Data getDataRegistro() {return dataRegistro;}

	public static Veiculo parseVeiculo(String s){
		String[] atributos = s.split(",");
		int id = Integer.parseInt(atributos[0]);
		String marca = atributos[1];
		String modelo = atributos[2];
		int ano = Integer.parseInt(atributos[3]);
		String categoria = atributos[4];
		String[] combustivel = atributos[5].split(";");
		int cilindros =  Integer.parseInt(atributos[6]);
		double cilindrada = Double.parseDouble(atributos[7]);
		String transmissao = atributos[8];
		String tracao = atributos[9];
		double consumoCidade = Double.parseDouble(atributos[10]);
		double consumoEstrada = Double.parseDouble(atributos[11]);
		double co2 = Double.parseDouble(atributos[12]);
		boolean turbo = Boolean.parseBoolean(atributos[13]);
		String datareg = atributos[14];

		Data dataRegistro = Data.parseData(datareg);

		return new Veiculo(id, marca, modelo, ano, categoria, combustivel, cilindros, cilindrada,
		transmissao, tracao, consumoCidade, consumoEstrada, co2, turbo, dataRegistro);
	}

	public String format(){
		String combustivelForm = "[";
		for(int i = 0; i < combustivel.length; i++){
			combustivelForm += combustivel[i];

			if(i < combustivel.length - 1){
				combustivelForm +=",";
			}
		}
		combustivelForm +="]";

		return String.format(java.util.Locale.US,
        		"[%d ## %s ## %s ## %d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %.2f ## %.2f ## %.1f ## %b ## %s]",
       			 this.id, this.marca, this.modelo, this.ano, this.categoria, combustivelForm,
       			 this.cilindros, this.cilindrada, this.transmissao, this.tracao,
      			  this.consumoCidade, this.consumoEstrada, this.co2, this.turbo, this.dataRegistro.format());
	}

}

class LeitorCsv{
	public static Veiculo[] ler(String caminhoArquivo) throws Exception {
		File fCount = new File(caminhoArquivo);
		Scanner sCount = new Scanner(fCount);

		if (sCount.hasNextLine()){
			sCount.nextLine();
		}
		int rowCount = 0;
		while(sCount.hasNextLine()){
			sCount.nextLine();
			rowCount++;
		}

		sCount.close();

		File f = new File(caminhoArquivo);
		Scanner sc = new Scanner(f);

		sc.nextLine();

		Veiculo[] veiculos = new Veiculo[rowCount];
		int cont = 0;

		while(sc.hasNextLine()){
			String linha = sc.nextLine();
			veiculos[cont] = Veiculo.parseVeiculo(linha);
			cont++;
		}

		sc.close();
		return veiculos;
	}
}

class questao4{
    public static int caseCmp(String str1, String str2){
        int resp = 0;
        int i = 0;
        while(resp == 0 && (i < str1.length() || i < str2.length())){
            if(i >= str1.length()){
                resp = -1;
            }
            else if(i >= str2.length()){
                resp = 1;
            }
            else{
                char c1 = str1.charAt(i);
                char c2 = str2.charAt(i);

                if(c1 >= 'A' && c1 <= 'Z') c1 += 32;
                if(c2 >= 'A' && c2 <= 'Z') c2 += 32;

                resp = c1 - c2;
            }
            i++;
        }
        return resp;
    }

    public static int comparar(Veiculo v1, Veiculo v2){
        int resp;
        if(v1.getCo2() < v2.getCo2()){
            resp = -1;
        }
        else if(v1.getCo2() > v2.getCo2()){
            resp = 1;
        }
        else{
            resp = caseCmp(v1.getTransmissao(), v2.getTransmissao());
        }
        return resp;
    }

    public static void swap(Veiculo[] arr, int i, int j){
        Veiculo tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void heapSort(Veiculo[] arr, int n){
        int k = Math.min(10, n);
        for(int tamHeap = 1; tamHeap < k; tamHeap++){
            construir(arr, tamHeap);
        }

        for(int i = k; i < n; i++){
            if(comparar(arr[i], arr[0]) < 0){
                swap(arr, 0, i);
                reconstruir(arr, k);
            }
        }

        int tamHeap = k;
        while(tamHeap > 1){
            swap(arr, 0, tamHeap - 1);
            tamHeap--;
            reconstruir(arr, tamHeap);
        }
    }

    private static void construir(Veiculo[] arr, int i){
        while(i > 0 && comparar(arr[i], arr[(i - 1) / 2]) > 0){
            swap(arr, i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    private static void reconstruir(Veiculo[] arr, int tamHeap){
        int i = 0;
        while(hasFilho(i, tamHeap)){
            int filho = getMaiorFilho(arr, i, tamHeap);
            if(comparar(arr[i], arr[filho]) < 0){
                swap(arr, i, filho);
                i = filho;
            }
            else{
                i = tamHeap;
            }
        }
    }

    private static boolean hasFilho(int i, int tamHeap){
        return (2 * i + 1) < tamHeap;
    }

    private static int getMaiorFilho(Veiculo[] arr, int i, int tamHeap){
        int filho = 2 * i + 1;
        if(filho + 1 < tamHeap && comparar(arr[filho + 1], arr[filho]) > 0){
            filho++;
        }
        return filho;
    }

	public static void main(String[] args) throws Exception {
		Veiculo[] veiculos = LeitorCsv.ler("../veiculos.csv");

		Scanner input = new Scanner(System.in);
		int procurado = input.nextInt();

        Veiculo[] achados = new Veiculo[veiculos.length];
        int qtdachados = 0;

		while(procurado != -1){
			boolean achou = false;
			for(int i = 0; i < veiculos.length && !achou; i++){
				if(veiculos[i].getId() == procurado){
					achados[qtdachados] = veiculos[i];
                    qtdachados++;
                    achou = true;
				}
			}
			procurado = input.nextInt();
		}

        heapSort(achados, qtdachados);
		int limite = Math.min(10, qtdachados);
        for(int i = 0; i < limite; i++){
            System.out.println(achados[i].format());
        }

		input.close();
	}
}
