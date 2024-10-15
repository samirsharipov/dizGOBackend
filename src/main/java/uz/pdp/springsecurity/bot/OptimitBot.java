package uz.pdp.springsecurity.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.springsecurity.entity.TelegramUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.enums.STATE;
import uz.pdp.springsecurity.repository.TelegramUserRepository;
import uz.pdp.springsecurity.repository.UserRepository;
import uz.pdp.springsecurity.utils.Constants;

import java.util.Objects;

//@Component
public class OptimitBot extends TelegramLongPollingBot {
    private TelegramUser telegramUser;
    private final TelegramUserRepository telegramUserRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public OptimitBot(TelegramBotsApi api, TelegramUserRepository telegramUserRepository, UserRepository userRepository, AuthenticationManager authenticationManager) throws TelegramApiException {
        this.telegramUserRepository = telegramUserRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        api.registerBot(this);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            telegramUser = getUserByChatId(chatId);
            if (message.hasText()) {
                String text = message.getText();
                if (text.equalsIgnoreCase("/start")) {
                    if (isLogin(chatId)) {
                        execute(SendMessage
                                .builder()
                                .parseMode(ParseMode.HTML)
                                .chatId(chatId)
                                .text("<b>Salom " + message.getFrom().getFirstName() + " \uD83D\uDC4B\uD83C\uDFFB \n" +
                                        "Optimit hisobot botiga Xush Kelibsiz \uD83E\uDD13 </b>")
                                .build());
                    } else {
                        execute(SendMessage
                                .builder()
                                .parseMode(ParseMode.HTML)
                                .chatId(chatId)
                                .text("<b>Salom " + message.getFrom().getFirstName() + " \uD83D\uDC4B\uD83C\uDFFB \n" +
                                        "Optimit hisobot botiga Xush Kelibsiz \uD83E\uDD13 \n" +
                                        "Saytdagi loginingizni kiriting \uD83D\uDC47\uD83C\uDFFB </b>")
                                .build());
                        telegramUser.setState(STATE.ENTER_LOGIN);
                        telegramUserRepository.save(telegramUser);
                    }
                } else if (telegramUser.getState().equals(STATE.ENTER_LOGIN)) {
                    telegramUser.setUsername(text);
                    telegramUser.setState(STATE.ENTER_PASSWORD);
                    telegramUserRepository.save(telegramUser);
                    execute(SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("<b>Parolingizni kiriting \uD83D\uDC47\uD83C\uDFFB</b>")
                            .parseMode(ParseMode.HTML)
                            .build());
                } else if (telegramUser.getState().equals(STATE.ENTER_PASSWORD)) {
                    try {
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(telegramUser.getUsername(), text));
                        User user = userRepository.findByUsername(telegramUser.getUsername()).orElseThrow();
                        user.setChatId(chatId);
                        userRepository.save(user);
                        execute(SendMessage
                                .builder()
                                .parseMode(ParseMode.HTML)
                                .text("<b>TABRIKLAYMIZ \uD83E\uDD73</b>\n\n" +
                                        "Siz endi hisobotlarni telegram orqali kuzatib borasiz \uD83E\uDD73")
                                .chatId(chatId)
                                .build());
                    } catch (Exception e) {
                        execute(SendMessage.builder().chatId(chatId).text("Login yoki parol xato").build());
                        execute(SendMessage.builder().chatId(chatId).text("Tizimga kirish uchun qaytadan loginingizni kiriting!").build());
                        telegramUser.setState(STATE.ENTER_LOGIN);
                        telegramUserRepository.save(telegramUser);
                    }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "OptimitgooBot";
    }

    @Override
    public String getBotToken() {
        return Constants.BOT_TOKEN;
    }

    private TelegramUser getUserByChatId(Long chatId) {
        TelegramUser byChatId = telegramUserRepository.findByChatId(chatId);
        return Objects.requireNonNullElseGet(byChatId, () -> telegramUserRepository.save(new TelegramUser(
                chatId,
                STATE.START
        )));
    }

    private boolean isLogin(Long chatId) {
        User user = userRepository.findByChatId(chatId);
        return user != null;
    }
}
///////////
