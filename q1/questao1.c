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
    char combustivel[2][MAX];
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

    token = strtok(NULL, ",");
    if(token) strcpy(tempCombustivel, token);

    token = strtok(NULL, ",");
    if(token) v.cilindros = atoi(token);

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
        if(strcmp(token, "true") == 0)
            v.turbo = true;
        else
            v.turbo = false;   
    }
    
    token = strtok(NULL, ",");
    if(token) {
        v.dataRegistro = parseData(token);
    }

    if (strlen(tempCombustivel) > 0) {
        char *token2 = strtok(tempCombustivel, ";");
        while(token2 != NULL && qtdComb < 2){
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

    int linhas = 0;
    char linha[1024];
    while(fgets(linha, sizeof(linha), arq) != NULL){
        if(strlen(linha) > 1) {
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

// Função auxiliar para comparar sem case sensitive
int caseCmp(char *s1, char *s2){
    int i = 0;
    while(s1[i] != '\0' || s2[i] != '\0'){
        char c1 = s1[i];
        char c2 = s2[i];

        if (c1 >= 'A' && c1 <= 'Z') c1 = c1 + 32;
        if (c2 >= 'A' && c2 <= 'Z') c2 = c2 + 32;

        if (c1 != c2) {
            return (c1 - c2);
        }
        i++;
    }
    return 0;
}

void SortModelo(Veiculo *arr, int n){
    int k = 10;
        for (int i = 0; i < k && i < n-1 ; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                int comp = caseCmp(arr[j].modelo, arr[min].modelo);
                if (comp < 0 || (comp == 0 && arr[j].id < arr[min].id)) {
                    min = j;
                }
            }
            if (min != i) {
                Veiculo temp = arr[i];
                arr[i] = arr[min];
                arr[min] = temp;
            }
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

    Veiculo *achados = (Veiculo*) malloc(n * sizeof(Veiculo));
    int procurado, qtdachados = 0;

    while(scanf("%d", &procurado) == 1 && procurado != -1){
        for(int i = 0; i < n; i++){
            if(veiculos[i].id == procurado){
                achados[qtdachados] = veiculos[i];
                qtdachados++;
                break; 
            }
        }
    }

    if(qtdachados > 0){
        SortModelo(achados, qtdachados);
        int limite = qtdachados < 10 ? qtdachados : 10;
        for(int i = 0; i < limite; i++){
            char resultado[1024];
            formatVeiculo(achados[i], resultado);
            printf("%s\n", resultado);
        }
    }

    free(achados);
    free(veiculos);
    return 0;
}