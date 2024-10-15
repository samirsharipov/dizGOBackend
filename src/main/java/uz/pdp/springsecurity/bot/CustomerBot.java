package uz.pdp.springsecurity.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.entity.CustomerTelegramUser;
import uz.pdp.springsecurity.enums.STATE;
import uz.pdp.springsecurity.repository.CustomerRepository;
import uz.pdp.springsecurity.repository.CustomerTelegramUserRepository;
import uz.pdp.springsecurity.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//@Component
public class CustomerBot extends TelegramLongPollingBot {
    private final   CustomerTelegramUserRepository customerTelegramUserRepository;
    private final CustomerRepository customerRepository;
    private CustomerTelegramUser telegramUser;

    @Autowired
    public CustomerBot(TelegramBotsApi api, CustomerTelegramUserRepository customerTelegramUserRepository, CustomerRepository customerRepository) throws TelegramApiException {
        this.customerTelegramUserRepository = customerTelegramUserRepository;
        this.customerRepository = customerRepository;
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
                                      "Optimit mijozlar botiga Xush Kelibsiz \uD83E\uDD13 </b>")
                                .build());
                    } else {
                        Message execute = execute(SendMessage
                                .builder()
                                .parseMode(ParseMode.HTML)
                                .chatId(chatId)
                                .replyMarkup(generateContactButton())
                                .text("<b>Salom " + message.getFrom().getFirstName() + " \uD83D\uDC4B\uD83C\uDFFB \n" +
                                      "Optimit mijozlar botiga Xush Kelibsiz \uD83E\uDD13 \n" +
                                      "Telefon raqamingizni kiriting \uD83D\uDC47\uD83C\uDFFB </b>")
                                .build());
                        telegramUser.setMessageId(execute.getMessageId());
                        telegramUser.setState(STATE.ENTER_LOGIN);
                        customerTelegramUserRepository.save(telegramUser);
                    }
                }
            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                String phoneNumber = contact.getPhoneNumber();
                phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
                System.out.println(phoneNumber);
                Optional<Customer> customer = customerRepository.findByPhoneNumberAndActiveIsTrueOrPhoneNumberAndActiveIsNull(phoneNumber, phoneNumber);
                execute(new DeleteMessage(chatId.toString(), telegramUser.getMessageId()));
                if (customer.isPresent()) {
                    customer.get().setChatId(chatId);
                    customerRepository.save(customer.get());
                    execute(SendMessage
                            .builder()
                            .parseMode(ParseMode.HTML)
                            .text("""
                                    <b>TABRIKLAYMIZ \uD83E\uDD73</b>
                                                                        
                                    Siz endi hisobotlarni telegram orqali kuzatib borasiz \uD83E\uDD73""")
                            .chatId(chatId)
                            .build());
                } else {
                    execute(SendMessage
                            .builder()
                            .parseMode(ParseMode.HTML)
                            .text("<b>Bu raqam mijozlar ro'yxatida mavjud emas!</b>")
                            .chatId(chatId)
                            .build());
                }
            }
        }
    }

    private ReplyKeyboard generateContactButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("Raqamni ulashish \uD83D\uDCDE");
        button.setRequestContact(true);
        row.add(button);
        rows.add(row);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return "OptimitMijozBot";
    }

    @Override
    public String getBotToken() {
        return Constants.CUSTOMER_BOT_TOKEN;
    }

    private CustomerTelegramUser getUserByChatId(Long chatId) {
        CustomerTelegramUser byChatId = customerTelegramUserRepository.findByChatId(chatId);
        return Objects.requireNonNullElseGet(byChatId, () -> customerTelegramUserRepository.save(new CustomerTelegramUser(
                chatId,
                STATE.START
        )));
    }

    private boolean isLogin(Long chatId) {
        Customer user = customerRepository.findByChatId(chatId);
        return user != null;
    }

}
