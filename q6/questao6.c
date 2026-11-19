#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#define MAX 100

// Definindo as structs de veículo e data
typedef struct{
    int ano;
    int mes;
    int dia;
} Data;

typedef struct{
    int id;
    char marca[MAX];
    char modelo[MAX];
    int ano;
    char categoria[MAX];
    char combustivel[2][MAX]; // Mantida a sua estrutura original
    int cilindros;
    double cilindrada;
    char transmissao[MAX];
    char tracao[MAX];
    double consumoCidade;
    double consumoEstrada;
    double co2;
    bool turbo;
    Data dataRegistro;
} Veiculo;

typedef struct No{
    Veiculo veiculo;
    struct No *esq;
    struct No *dir;
} No;

No* novoNo(Veiculo v){
    No *novoNo = (No*) malloc(sizeof(No));
    if(novoNo == NULL){
        printf("Erro ao alocar memória para o nó\n");
        exit(1);
    }
    novoNo->veiculo = v;
    novoNo->esq = NULL;
    novoNo->dir = NULL;
    return novoNo;
}

No* raiz;

void start(){
    raiz = NULL;
}

void inserirRecursivo(No **no, Veiculo v){
    if(*no == NULL){
        *no = novoNo(v);
        return;
    }
    if(strcmp(v.modelo, (*no)->veiculo.modelo) < 0){
        inserirRecursivo(&((*no)->esq), v);
    } else if (strcmp(v.modelo, (*no)->veiculo.modelo) > 0){
        inserirRecursivo(&((*no)->dir), v);
    }
}

void inserir(Veiculo v){
    inserirRecursivo(&raiz, v);
}

void buscarRecursivo(No *no, char *modelo){
    
    if(no == NULL){
        printf("NAO\n");
    }
    if(strcmp(modelo, no->veiculo.modelo) == 0){
        printf("SIM\n");
    }
    if(strcmp(modelo, no->veiculo.modelo) < 0){
        printf("esq ");
        buscarRecursivo(no->esq, modelo);
    } else {
        printf("dir ");
        buscarRecursivo(no->dir, modelo);
    }
}
void buscar(char *modelo){
    printf("raiz ");
    buscarRecursivo(raiz, modelo);
}

void mostrarOrdemRecursivo(No *no){
    if(no != NULL){
        mostrarOrdemRecursivo(no->esq);
        char resultado[1024];
        formatVeiculo(no->veiculo, resultado);
        printf("%s\n", resultado);
        mostrarOrdemRecursivo(no->dir);
    }
}

void mostrarOrdem(){
    mostrarOrdemRecursivo(raiz);
}



//-----------------------------------
// Funções para a data:
Data parseData(char *s){
    Data novaData = {0, 0, 0};
    if (s) {
        sscanf(s, "%d-%d-%d", &novaData.ano, &novaData.mes, &novaData.dia);
    }
    return novaData;
}

void formatData(Data d, char *buffer){
    sprintf(buffer, "%02d/%02d/%04d", d.dia, d.mes, d.ano);
}

//-----------------------------------
// Funcoes do veiculo
Veiculo parseVeiculo(char* s){
    Veiculo v;
    // Inicializa a struct com zeros para limpar resíduos de memória nas strings
    memset(&v, 0, sizeof(Veiculo));
    int qtdComb = 0;

    char *token;
    char tempCombustivel[MAX * 2] = "";
    
    token = strtok(s, ",");
    if(token) v.id = atoi(token);
    
    token = strtok(NULL, ",");
    if(token) strcpy(v.marca, token);
    
    token = strtok(NULL, ",");
    if(token) strcpy(v.modelo, token);
    
    token = strtok(NULL, ",");
    if(token) v.ano = atoi(token);

    token = strtok(NULL, ",");
    if(token) strcpy(v.categoria, token);

    // 1. APENAS COPIA o texto dos combustíveis para processar no final da função
    token = strtok(NULL, ",");
    if(token) strcpy(tempCombustivel, token);

    // 2. CONTINUA extraindo os campos da linha principal normalmente
    token = strtok(NULL, ",");
    if(token) v.cilindros = atoi(token); // Corrigido erro de digitação do campo original se aplicável

    token = strtok(NULL, ",");
    if(token) v.cilindrada = atof(token);

    token = strtok(NULL, ",");
    if(token) strcpy(v.transmissao, token);

    token = strtok(NULL, ",");
    if(token) strcpy(v.tracao, token);

    token = strtok(NULL, ",");
    if(token) v.consumoCidade = atof(token);

    token = strtok(NULL, ",");
    if(token) v.consumoEstrada = atof(token);

    token = strtok(NULL, ",");
    if(token) v.co2 = atof(token);

    token = strtok(NULL, ",");
    if(token){
        // Remove quebras de linha residuais (\r ou \n) que quebram o strcmp
        token[strcspn(token, "\r\n")] = '\0';
        if(strcmp(token, "true") == 0)
            v.turbo = true;
        else
            v.turbo = false;   
    }
    
    token = strtok(NULL, ",");
    if(token) {
        token[strcspn(token, "\r\n")] = '\0';
        v.dataRegistro = parseData(token);
    }

    // 3. AGORA SIM, com a linha principal totalmente lida, dividimos os combustíveis
    if (strlen(tempCombustivel) > 0) {
        char *token2 = strtok(tempCombustivel, ";");
        while(token2 != NULL && qtdComb < 2){
            token2[strcspn(token2, "\r\n")] = '\0'; // Limpa quebras invisíveis
            if (strlen(token2) > 0) {
                strcpy(v.combustivel[qtdComb], token2);
                qtdComb++;
            }
            token2 = strtok(NULL, ";");
        }
    }
    
    return v;   
}

void formatVeiculo(Veiculo c, char *resultado) {
    char combustivelForm[70];
    char dataForm[12];
    int posicao = 0;

    posicao += sprintf(&combustivelForm[posicao], "[");
    for (int i = 0; i < 2; i++) {
        if (c.combustivel[i][0] == '\0') {
            break;
        }
        // Se for o primeiro item E houver um segundo item válido adiante
        if (i == 0 && c.combustivel[1][0] != '\0') {
            posicao += sprintf(&combustivelForm[posicao], "%s,", c.combustivel[i]);
        } else {
            posicao += sprintf(&combustivelForm[posicao], "%s", c.combustivel[i]);
        }
    }
    sprintf(&combustivelForm[posicao], "]");
    
    formatData(c.dataRegistro, dataForm);
    
    sprintf(resultado, 
        "[%d ## %s ## %s ## %d ## %s ## %s ## %d ## %.1f ## %s ## %s ## %.2f ## %.2f ## %.1f ## %s ## %s]", 
        c.id, c.marca, c.modelo, c.ano, c.categoria, combustivelForm, 
        c.cilindros, c.cilindrada, c.transmissao, c.tracao, 
        c.consumoCidade, c.consumoEstrada, c.co2, 
        c.turbo ? "true" : "false", dataForm
    );
}

//-----------------------------------
// funcoes do csv
int contarLinhas(char* caminhoArquivo){
    FILE *arq = fopen(caminhoArquivo, "r");
    if(arq == NULL){
        printf("Erro ao abrir o arquivo\n");
        return 0;
    }

    int sizeof_linha = 0;
    int linhas = 0;
    char c;
    while((c = fgetc(arq)) != EOF ){
        if(c == '\n'){
            linhas++;
        }
    }
    fclose(arq);
    return linhas;
}

Veiculo* lerCsv(char *caminhoArquivo, int *n){
    int numLinhas = contarLinhas(caminhoArquivo);
    if(numLinhas < 1){
        *n = 0;
        return NULL;
    }

    *n = numLinhas - 1; 

    Veiculo *veiculos = (Veiculo*) malloc((*n) * sizeof(Veiculo));
    if(veiculos == NULL){
        *n = 0;
        return NULL;
    }

    FILE *arquivo = fopen(caminhoArquivo, "r");
    if(arquivo == NULL){
        free(veiculos);
        *n = 0;
        return NULL;
    }

    char linha[1024];
    fgets(linha, sizeof(linha), arquivo); // Pula linha de cabeçalho
    
    int i = 0;
    while(fgets(linha, sizeof(linha), arquivo) != NULL && i < (*n)){
        veiculos[i] = parseVeiculo(linha);
        i++;
    }

    fclose(arquivo);
    return veiculos;
}

void liberarArvore(No *no){
    if(no != NULL){
        liberarArvore(no->esq);
        liberarArvore(no->dir);
        free(no);
    }
}

int main(){
    char *caminhoCSV = "/tmp/veiculos.csv";
    int n = 0;
    Veiculo *veiculos = lerCsv(caminhoCSV, &n);

    if(veiculos == NULL) {
        printf("Nenhum veiculo carregado.\n");
        return 1;
    }

    int procurado;

    start();

    while(scanf("%d", &procurado) == 1 && procurado != -1){
        for(int i = 0; i < n; i++){
            if(veiculos[i].id == procurado){
                inserir(veiculos[i]);
                break; 
            }
        }
    }
    char modelo[MAX];
    while(scanf(" %[^\n]", modelo) == 1 && strcmp(modelo, "FIM") != 0){
        buscar(modelo);
    }
    mostrarOrdem();
    liberarArvore(raiz);
    raiz = NULL;
    free(veiculos);
    return 0;
}

