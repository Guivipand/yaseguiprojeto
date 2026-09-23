Produto(Model model){
        List<Produto>@Controller
public class Atividade8Controller {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/formularioProduto")
    public String formularioProduto(){
        return"formularioProduto";
    }

    @PostMapping("/Produtos")
    public String saveProdutos(@RequestParam String titulo, @RequestParam String descricao, @RequestParam String preco) {
        produtoRepository.save(new Produto(titulo, descricao, preco));
        return "redirect:/formularioProduto";
    }

    @GetMapping("/listaProdutos")
    public String lP listaProduto = produtoRepository.findAll();
        model.addAttribute("listaProduto",listaProduto);
        return "listaProduto";
    }

    @GetMapping("/editarProduto")
    public String editarProduto(@RequestParam int id, Model model){
        Produto produto = produtoRepository.findByID(id);
        model.addAttribute("produto", produto);
        return "editarProduto";
    }

    @PostMapping("/atualizarProduto")
    public String atualizarProduto(@RequestParam int id, @RequestParam String descricao, @RequestParam String preco){

        Produto produto = produtoRepository.findByID(id);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produtoRepository.update(produto);
        return "redirect:/listaProdutos";
    }

    @GetMapping("/excluirProduto")
    public String excluirProduto(@RequestParam int id){
        produtoRepository.delete(id);
        return "redirect:/listaProdutos";
    }

}