package tarefas;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CodexSeleniumBot {

    // URL da tela principal do Codex (ajuste para a URL real do seu ambiente)
    private static final String CODEX_URL = "https://chat.openai.com/codex";

    public static void main(String[] args) {

        // Texto da tarefa que você quer criar
        String taskText = (args.length > 0) ? String.join(" ", args)
                : "Ajustar formulário de caixa para listar usuários";

        // 1) Caminho do driver local (drivers/chromedriver.exe)
        String userDir = System.getProperty("user.dir");
        Path driverPath = Paths.get(userDir, "drivers", isWindows() ? "chromedriver.exe" : "chromedriver");
        System.setProperty("webdriver.chrome.driver", driverPath.toString());

        // 2) Configuração do Chrome
        ChromeOptions options = new ChromeOptions();
        // (opcional) apontar para o binário do Chrome
        options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        // (opcional) usar perfil para manter login
        Path chromeProfileDir = Paths.get(userDir, "pw-profile");
        options.addArguments("--user-data-dir=" + chromeProfileDir.toAbsolutePath());
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, 30);
        Actions actions = new Actions(driver);

        try {
            // 3) Acessa a página
            driver.get(CODEX_URL);

            // 4) Campo "Descreva uma tarefa"
            WebElement input = waitForTaskInput(wait);
            input.click();
            input.clear();
            input.sendKeys(taskText);
            input.sendKeys(Keys.ENTER);

            // 5) Espera aparecer a nova tarefa e clica
            WebElement newTask = waitForTaskInList(wait, taskText);
            newTask.click();

            // 6) Espera botão "Criar PR" e clica
            WebElement btnCriarPR = waitForCreatePrButton(wait);
            btnCriarPR.click();

            System.out.println("Fluxo concluído: tarefa criada e PR solicitado.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // driver.quit(); // descomente para fechar o navegador no final
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    // ---------- Helpers ----------

    private static WebElement waitForTaskInput(WebDriverWait wait) {
        By byPlaceholderPT = By.xpath("//textarea[@placeholder='Descreva uma tarefa']");
        return wait.until(ExpectedConditions.presenceOfElementLocated(byPlaceholderPT));
    }

    private static WebElement waitForTaskInList(WebDriverWait wait, String taskText) {
        By byContains = By.xpath("//div[contains(text(), " + escapeForXPath(taskText) + ")]");
        return wait.until(ExpectedConditions.presenceOfElementLocated(byContains));
    }

    private static WebElement waitForCreatePrButton(WebDriverWait wait) {
        By byCriarPR = By.xpath("//button[contains(.,'Criar PR') or contains(.,'Create PR')]");
        return wait.until(ExpectedConditions.elementToBeClickable(byCriarPR));
    }

    private static String escapeForXPath(String s) {
        if (!s.contains("'")) return "'" + s + "'";
        if (!s.contains("\"")) return "\"" + s + "\"";
        String[] parts = s.split("'");
        StringBuilder sb = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            sb.append("'").append(parts[i]).append("'");
            if (i < parts.length - 1) sb.append(", \"'\", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
