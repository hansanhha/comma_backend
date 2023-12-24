package know_wave.comma.common.notification.push.service;

import jakarta.transaction.Transactional;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.notification.push.dto.AccountEmailNotificationRequest;
import know_wave.comma.common.notification.push.dto.PushNotificationRequest;
import know_wave.comma.common.notification.push.entity.NotificationFeature;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PushNotificationGateway {

    private final PushNotificationOptionReader pushNotificationOptionReader;
    private final PushNotificationManager pushNotificationManager;
    private final AccountQueryService accountQueryService;
    private final PushNotificationLogger pushNotificationLogger;

    public void notify(PushNotificationRequest notificationRequest) {
        String accountId = notificationRequest.getAccountId();
        Account account = accountQueryService.findAccount(accountId);

        if (pushNotificationOptionReader.isSendableTime(account)
                && pushNotificationOptionReader.isAllowFeature(account, notificationRequest.getNotificationFeature())) {

            Set<PushNotificationType> allowedTypes = pushNotificationOptionReader.getAllowedTypes(account);

            pushNotificationManager.send(notificationRequest, allowedTypes);
            pushNotificationLogger.log();
        }
    }

    public void accountEmailVerifyNotification(AccountEmailNotificationRequest notificationRequest) {
        pushNotificationManager.sendVerifyEmail(notificationRequest);
        pushNotificationLogger.log();
    }

    public void getNotificationHistory() {

    }
}
