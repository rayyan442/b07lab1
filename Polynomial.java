import java.io.*;
public class Polynomial {
    private double[] coefficients;
    private int[] exponents;


    public Polynomial() {
        this.coefficients = new double[]{0};
        this.exponents = new int[]{0};
    }
    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients=new double[coefficients.length];
        this.exponents=new int[exponents.length];
        for (int i = 0; i < coefficients.length; i++) {
            this.coefficients[i] = coefficients[i];
            this.exponents[i] = exponents[i];
        }
    }
    public Polynomial(File file) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        reader.close();

        line = line.replace("-", "+-");
        String[] terms = line.split("\\+");
        int count = 0;
        for (String term : terms) {
            if(!term.isEmpty()) {
                count++;
            }
        }
        this.coefficients = new double[count];
        this.exponents = new int[count];
        int index = 0;
        for (String term: terms){
            if(term.isEmpty()) continue;
            if (term.contains("x")){
                String[]parts = term.split("x");
                if(parts[0].equals("") || parts[0].equals("+")){
                    this.coefficients[index] = 1;
                }else if (parts[0].equals("-")){
                    this.coefficients[index] = -1;
                }else{
                    this.coefficients[index] = Double.parseDouble(parts[0]);
                }
                if(parts.length > 1 && !parts[1].equals("")){
                    this.exponents[index] = Integer.parseInt(parts[1]);
                }else{
                    this.exponents[index] = 1;
                }
            }else{
                this.coefficients[index] = Double.parseDouble(term);
                this.exponents[index] = 0;
            }
            index++;
        }
        combine();

       
        
    }
    public Polynomial add(Polynomial other){
        double[] tempCoeffs = new  double[this.coefficients.length + other.coefficients.length];
        int[] tempExps = new int[this.exponents.length + other.exponents.length];
        int size = 0;
        
        for(int i = 0; i<this.coefficients.length; i++){
            tempCoeffs[size] = this.coefficients[i];
            tempExps[size] = this.exponents[i];
            size++;
        }
        for(int j = 0; j<other.coefficients.length; j++){
            int exp = other.exponents[j];
            double coeff = other.coefficients[j];
            boolean found = false;

            for(int k =0; k<size; k++){
                if(tempExps[k] == exp){
                    tempCoeffs[k] += coeff;
                    found = true;
                    break;
                }
            }
            if(!found){
                tempCoeffs[size] = coeff;
                tempExps[size] = exp;
                size++;
            }
        }
        int count = 0;
        for(int i = 0; i<size; i++){
            if(tempCoeffs[i] != 0){
                count++;
            }
        }
        double[] newCoeffs = new double[count];
        int[] newExps = new int[count];
        int index = 0;
        for(int i = 0; i<size; i++){
            if(tempCoeffs[i] != 0){
                newCoeffs[index] = tempCoeffs[i];
                newExps[index] = tempExps[i];
                index++;
            }
        }
        return new Polynomial(newCoeffs, newExps);

         
    }
    
    public double evaluate(double x){
        double sum = 0;
        for (int i = 0; i < this.coefficients.length; i++){
            sum += this.coefficients[i] * Math.pow(x, this.exponents[i]);
        }
        return sum;
    }
    public boolean hasRoot(double x){
        return this.evaluate(x) == 0;
    }

    public Polynomial multiply(Polynomial other){
        double[] tempCoeffs = new double[this.coefficients.length * other.coefficients.length];
        int[] tempExps = new int[this.exponents.length * other.exponents.length];
        int size = 0;

        for(int i = 0; i<this.coefficients.length; i++){
            for (int j = 0; j < other.coefficients.length; j++){
                int exp = this.exponents[i] + other.exponents[j];
                double coeff = this.coefficients[i] * other.coefficients[j];
                boolean found = false;

                for(int k = 0; k<size; k++){
                    if(tempExps[k] == exp){
                        tempCoeffs[k] += coeff;
                        found = true;
                        break;
                    }
                }
                if(!found){
                    tempCoeffs[size] = coeff;
                    tempExps[size] = exp;
                    size++;
                }
            }
        }
        int count = 0;
        for(int i = 0; i<size; i++){
            if(tempCoeffs[i] != 0){
                count++;
            }
        }
        double[] newCoeffs = new double[count];
        int[] newExps = new int[count];
        int index = 0;
        for(int i = 0; i<size; i++){
            if(tempCoeffs[i] != 0){
                newCoeffs[index] = tempCoeffs[i];
                newExps[index] = tempExps[i];
                index++;
            }
        }
        return new Polynomial(newCoeffs, newExps);
    }


    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            double c = coefficients[i];
            int e = exponents[i];

            if (i > 0 && c >= 0) sb.append("+");

            if (e == 0) {
                sb.append(c);
            } else {
                if (c == 1) sb.append("x");
                else if (c == -1) sb.append("-x");
                else sb.append(c).append("x");
                if (e != 1) sb.append(e);
            }
        }

        writer.write(sb.toString());
        writer.close();
    }
        


    private void combine(){
        int maxExp = 0;
        for (int exp : this.exponents){
            if(exp > maxExp){
                maxExp = exp;
            }
        }
        double[] temp = new double[maxExp + 1];
        for (int i = 0; i < coefficients.length; i++) {
            temp[exponents[i]] += coefficients[i];
        }
        int count = 0;
        for (double c: temp){
            if(c!=0) count++;

        }
        double[] newCoeffs = new double[count];
        int[] newExps = new int[count];
        int idx = 0;
        for (int e = 0; e < temp.length; e++) {
            if (temp[e] != 0) {
                newCoeffs[idx] = temp[e];
                newExps[idx] = e;
                idx++;
            }
        }
        coefficients = newCoeffs;
        exponents = newExps;
    }


        
    
}