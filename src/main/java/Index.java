
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author marcos
 */
public class Index {

    private String path;
    private NoTable[] tables;
    private int nroTables;

    public Index(String path, int nroTables) {
        this.path = path;
        this.nroTables = nroTables;
        this.tables = new NoTable[this.nroTables];
    }

    void ReadDump() throws IOException {
        int j = 0;
        String nameTable, aux, text;
        int posT=0 , posE;
        String[] filds, tab, info, pkey, line;
        NoTable t;
        NoRecord r;
        NoRecord[] records;
        
        text = new String(Files.readAllBytes(Paths.get(this.path)), StandardCharsets.UTF_8);
        line = text.split("\n");
        for (int i = 0; i < line.length; i++) {
            if (line[i].contains("CREATE TABLE ")) {//estamos em uma tabela
                nameTable = line[i].replace("CREATE TABLE ", "");
                nameTable = nameTable.replace(" (", "");
                t = new NoTable(nameTable); //novo nó tabela com nome lido 
                i++; j=i;
                while (!line[j].equals(");")) j++;
                filds = new String [j-i]; j=0; // quantidade de campos na tabela
                while (!line[i].equals(");")) {//leitura de todos os campos em um vetor de string
                    filds[j]= line[i].split(" ")[4];
                    i++; j++;
                }
                t.setFilds(filds);//armazena o nome dos campos em um vetos de string na tabela t criada
                //hashing para tabela
                this.tables[posT]=t; 
                posT++;
                
            } else if (line[i].contains("COPY ")) {//estou na lista de elementos de uma tabela
                nameTable = line[i].split(" ")[1];//nome da tabela que se trata os elementos
                t = getTable(nameTable);//retorna o NoTable com aquele nome ps: a função getTable será com hashing
                i++;
                j=i;
                while (!line[j].equals("\\.")) j++;
                t.setNroElements(j-i);//numero de elementos daquela tabela
                records = new NoRecord[j-i];//vetor de registros instanciado com a quantidade de elementos
                j=0; 
                while (!line[i].equals("\\.")) {
                    line[i] = line[i].replace("\t", "\t ");
                    info = line[i].split("\t");//separa campos do registro
                    r = new NoRecord(info[0]);//inicialmente o id será o campo 0 das informações do registro
                    r.setInfo(info);//vetor de informações de um registro
                    records[j] = r; 
                    i++; j++;
                }
                t.setElements(records);
                
                
            } else if (line[i].contains("ALTER TABLE ONLY ")) {
                nameTable = line[i].split(" ")[3];
                //System.out.println("\n"+nameTable);
                i++;
                aux = line[i].replace("(","\t");
                aux = aux.replace(")", "\t");
                aux = aux.split("\t")[1];
                if (line[i].contains(", ")) {
                    pkey = aux.split(", ");
                } else {
                    pkey = new String[1];
                    pkey[0] = aux;
                }
                /*
                for (int j = 0; j < pkey.length; j++) {
                    System.out.print(pkey[j] + "\t");
                }
                System.out.println();*/
            }
        }
    }

    /**
     * @return the tables
     */
    public NoTable[] getAllTables() {
        return tables;
    }

    /**
     * @param tables the tables to set
     */
    public void setAllTables(NoTable[] tables) {
        this.tables = tables;
    }
    
    public NoTable getTable (String name){
        for (int i = 0 ; i < this.tables.length ; i++){
            //System.out.println(this.tables[i].getName());
            if(this.tables[i].getName().equals(name)){
                return this.tables[i];
            }
        }
        return null;
    }
    
    public void PrintStruct (){
        for(int i =0 ; i < this.tables.length ; i++){
            System.out.println("\n\n"+this.tables[i].getName());
            for(int j = 0 ; j < this.tables[i].getElements().length ; j++){
                System.out.print(this.tables[i].getElements()[j].getId()+" ");
            }
        }
    }

}
