package com.curso.requisicoeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button btnRecuperar;
    private TextView txtLogradouro,txtUf,txtCep,txtLocalidade,txtBairro,txtComplemento;
    //para usar API e melhor sempre criar uma Threads semparada
    //sempre tem q perminir no manifest que o app use internet
    //esse codigo serva para qualquer API oq muda e a forma que vc ira mostrar
    //por ex: vc quer pegar o cep de alguem, vc so coloca uma editText e recupera
    //oq foi digitado, msm q a api seja sem ou com (Sem parâmetros), sempre e recuperado
    //do msm jeito
    /*
    Exemplo de como recuperar um cep digitado pelo usuario
    String cep = editCep.getText().toString();
    String urlCep = "https://viacep.com.br/ws/"+cep+"/json/";
    dessa forma pode tambem recuperar dados pesquisado pelo usuario
    No site da API ela mostra como recuperar dados pesquisando
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRecuperar = findViewById(R.id.buttonRecuperar);
        txtLogradouro = findViewById(R.id.logradouro);
        txtComplemento = findViewById(R.id.complemento);
        txtCep = findViewById(R.id.cp);
        txtBairro = findViewById(R.id.bairro);
        txtUf = findViewById(R.id.uf);
        txtLocalidade = findViewById(R.id.localidade);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTask myTask = new MyTask();
                String urlApi = "https://blockchain.info/ticker";
                String urlCep = "https://viacep.com.br/ws/18079728/json/";//obrigatorio para pegar os dados da api
                myTask.execute(urlCep);
            }
        });
    }

    class MyTask extends AsyncTask<String,Void,String> {
        //class que cria uma Threads
        //ela pode executar dados em sem travar o app
        //tipo, uma progressBar carrega enquanto o usuario pode clicar ou fazer outras coisas
        @Override
        protected String doInBackground(String... strings) {
            //dessa forma ele se conecta com a API
            String stringUrl = strings[0];
            InputStreamReader inputStreamReader = null;
            InputStream inputStream = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL(stringUrl);
                //esse openConnection() funciona como se ele fizesse a busca no site passado
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //recupera os dados que foram pegos da API/site
                //ele recupera os dados em formato Bytes
                inputStream = conexao.getInputStream();

                //convert o inputStream, ele le os dados e convert para letras e numeros
                inputStreamReader = new InputStreamReader(inputStream);

                //ele ler letra a letra do inputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();

                //ler linha a linha dos dados q esta no BufferedReader
                String linha = "";

                while ((linha = reader.readLine()) != null){
                    //A "linha" ira receber os dados do reader, o while ira repetir isso
                    //ate que reader esteja vazio, dessa forma  a "linha" ira recebar os
                    //dados que vem da API, não importa quantas linhas seja.

                    //as linhas de txt pega pela String linha, sao add nessa buffer
                    //ela organiza uma por uma, cria um monte de String em apenas uma
                    //tipo um array, quando passar por tudo isso ai pode add em um TextView
                    buffer.append(linha);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Criando um JsonObject para tratar a forma que ira ser apresentado os dados
            String logadouro = null;
            String complemento = null;
            String cep = null;
            String bairro = null;
            String localidade = null;
            String uf = null;
            try {
                JSONObject jsonObject = new JSONObject(s);
                logadouro = jsonObject.getString("logradouro");
                complemento = jsonObject.getString("complemento");
                cep = jsonObject.getString("cep");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");
                //ao fazer dessa forma, ele recupera a chave e pega os dados,
                //ex: logradouro esta salvo na API, com a rua sendo o valor guardado
                //ao usar uma String e usar o JSONObject e passar o nome da chave,
                //vc conseggui recuperar o valor que esta na chave informada



            } catch (JSONException e) {
                e.printStackTrace();
            }
            //mostrando os dados recuperados para o usuario
            txtLogradouro.setText(logadouro);
            txtBairro.setText(bairro);
            txtCep.setText(cep);
            txtLocalidade.setText(localidade);
            txtUf.setText(uf);
            txtComplemento.setText(complemento);
        }
    }
}