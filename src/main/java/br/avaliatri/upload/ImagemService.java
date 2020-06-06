package br.avaliatri.upload;

import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Imagem;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ImagemService {
    private static final Logger log = Logger.getLogger(ImagemService.class.getName());

    @Value("${contato.disco.raiz}")
    private String dir;

    @Value("${img.size}")
    private Integer size;

    final static String EXTENSOES_VALIDAS = "jpgpngjpeg";

    public String store(Img img) throws Excecao {

        if(!EXTENSOES_VALIDAS.contains(img.getExtensao().toLowerCase())) {
            throw new Excecao("Formato de arquivo " + img.getExtensao() + " não é valido");
        }
        return img.getPastaImagem();
    }

    private BufferedImage redimensionarImagem(BufferedImage imagem, int size) {
        return Scalr.resize(imagem, Scalr.Method.ULTRA_QUALITY, size);
    }

    private String pegarExtensaoArquivo(MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
    }

    private String formatNomeArquivoComExt(MultipartFile file) {
        return this.formatNomeArquivoSemExt(file) + "." + pegarExtensaoArquivo(file);
    }

    private String formatNomeArquivoSemExt(MultipartFile file) {
        String nome = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".") -1);
        return nome
                .trim()
                .replaceAll("[^\\w\\d\\s]", "")
                .replaceAll("[0-9]", "")
                .replaceAll("[\\s]+", "_")
                .toLowerCase();
    }

    public Img gerarImagem(MultipartFile file, Integer questao_id) throws Excecao {
        return this.retornoImg(file,  "questao_"+questao_id + "--" + System.currentTimeMillis());
    }

    public Img gerarImagem(MultipartFile file, Integer questao_id, Integer alternativa_id) throws Excecao {
        return this.retornoImg(file,  "questao_"+questao_id + "-" + "alternativa_" + alternativa_id + "--" + System.currentTimeMillis());
    }

    public Img retornoImg(MultipartFile file, String path) throws Excecao {
        Img retorno = new Img();
        retorno.setNomeCompleto(this.formatNomeArquivoComExt(file));
        retorno.setNomeSemExtensao(this.formatNomeArquivoSemExt(file));
        retorno.setExtensao(this.pegarExtensaoArquivo(file));
        retorno.setPastaImagem(File.separator + path + "_" + retorno.getNomeCompleto());
        retorno.setArquivo(this.converterParaFile(file, dir + retorno.getPastaImagem()));
        return retorno;
    }

    private File converterParaFile(MultipartFile file, String dir) throws Excecao {
        File res = new File(dir);
        res.mkdirs();
        try {
            file.transferTo(res);
        } catch (IOException e) {
            throw new Excecao("Erro ao converter arquivo para File");
        }
        return res;
    }

    public boolean deletarImagem(Imagem imagem) throws Excecao {
        boolean deletado = true;
        File arquivoASerDeletado = new File(dir + File.separator + imagem.getCaminhoArquivo());

        if(arquivoASerDeletado.isFile()) {
            deletado = arquivoASerDeletado.delete();
        } else {
            log.log(Level.SEVERE, "Não foi possivel excluir o arquivo " + imagem.getCaminhoArquivo() + " no diretorio " + dir + File.separator + imagem.getCaminhoArquivo());
            throw new Excecao("Não foi possivel excluir o arquivo " + imagem.getCaminhoArquivo());
        }

        if(arquivoASerDeletado.exists()) {
            log.log(Level.SEVERE, "Arquivo foi não foi excluido corretament " + imagem.getCaminhoArquivo() + " no diretorio " + dir + File.separator + imagem.getCaminhoArquivo());
            throw new Excecao("Arquivo foi não foi excluido corretamente " + imagem.getCaminhoArquivo());
        }

        return deletado;
    }
}
