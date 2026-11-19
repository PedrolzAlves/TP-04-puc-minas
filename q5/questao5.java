import java.util.Scanner;
import java.io.File;

public class questao5{

	public static void main(String[] args) throws Exception {
		ArvoreBinaria arvore = new ArvoreBinaria();
		Veiculo[] veiculos = LeitorCsv.ler("../veiculos.csv");
	
		Scanner input = new Scanner(System.in);
		int procurado = input.nextInt();

		while(procurado != -1){
			for(int i = 0; i < veiculos.length; i++){
				if(veiculos[i].getId() == procurado){
					arvore.inserir(veiculos[i]);
					break;
				}
			}
			procurado = input.nextInt();
		}
		input.nextLine(); 
		String modeloProcurado = input.nextLine().trim();
		while(!modeloProcurado.equals("FIM")){
			String resultado = arvore.buscar(modeloProcurado);
			System.out.println(resultado);
			modeloProcurado = input.nextLine().trim();
		}

		arvore.mostrarOrdem();
		input.close();
	}
}

class No{
    
    //Atributos da classe No
    public Veiculo veiculo;
    public No esq;
    public No dir;

    //Construtor da classe No
    public No(Veiculo veiculo){
        this.veiculo = veiculo;
        this.esq = null;
        this.dir = null;
    }



}

class ArvoreBinaria{
	
	//Atributos da classe ArvoreBinaria
	private No raiz;

	//Construtor da classe ArvoreBinaria
	public ArvoreBinaria(){
		this.raiz = null;
	}

	//Metodo para inserir um veiculo na arvore
	public void inserir(Veiculo veiculo){
		this.raiz = inserir(this.raiz, veiculo);
	}

	//Metodo  para inserir um veiculo na arvore
	private No inserir(No no, Veiculo veiculo){
		if(no == null){
			return new No(veiculo);
		}
		if(veiculo.getModelo().compareTo(no.veiculo.getModelo()) < 0){
			no.esq = inserir(no.esq, veiculo);
		} else if(veiculo.getModelo().compareTo(no.veiculo.getModelo()) > 0){
			no.dir = inserir(no.dir, veiculo);
		}
		return no;
	}

	public String buscar(String modelo){
		return buscar(this.raiz, modelo);
	}

	private String buscar(No no, String modelo){
		String resultado = (no == this.raiz) ? "raiz " : "";
		if(no == null){
			return resultado + "NAO";
		}

		if(modelo.equals(no.veiculo.getModelo())){
			return resultado + "SIM";
		}

		if(modelo.compareTo(no.veiculo.getModelo()) < 0){
			return resultado + "esq " + buscar(no.esq, modelo);
		} else {
			return resultado + "dir " + buscar(no.dir, modelo);
		}
	}

	public void mostrarOrdem(){
		mostrarOrdem(this.raiz);
	}

	public void mostrarOrdem(No no){
		if(no != null){
			mostrarOrdem(no.esq);
			System.out.println(no.veiculo.format());
			mostrarOrdem(no.dir);
		}
	}
	




} 

//Criacao da classe data para usar na classe Veiculo
class Data{

	//Atributos da classe data
	private int ano;
	private int mes;
	private int dia;

	//construtor da classe data
	public Data(int ano, int mes, int dia){
		this.ano = ano;
		this.mes = mes;
		this.dia = dia;
	}

	//Metodos getters da classe
	public int getAno(){ return ano; }
	public int getMes() { return mes; }
	public int getDia() { return dia; }

	//Metodo para formatar a data
	public String format(){
		return String.format("%02d/%02d/%02d", this.dia, this.mes, this.ano);
	}

	//Cria o novo objeto data para o veiculo
	public static Data parseData(String s){
		String[] partes = s.split("-");
		int ano = Integer.parseInt(partes[0]);
		int mes = Integer.parseInt(partes[1]);
		int dia = Integer.parseInt(partes[2]);

		return new Data(ano, mes, dia);
	}
	
}

class Veiculo{
	//Atributos da classe
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

	//Construtor
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

	//Getters para a clase
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

	//Metodo para criar um novo objeto
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

		//OBS: Consultei a IA para descobrir como formatava a virgula em ponto com esse util.
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

		String cabecalho = sc.nextLine();

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

