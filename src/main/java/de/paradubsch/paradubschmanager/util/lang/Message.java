package de.paradubsch.paradubschmanager.util.lang;

public class Message {
    public enum Error implements BaseMessageType {
        CMD_LANGUAGE_NOT_PROVIDED("cmdLanguageNotProvided", "&cDu musst eine Sprache angeben!"),
        CMD_LANGUAGE_NOT_FOUND("cmdLanguageNotFound", "Die Sprache &d%1 &7konnte &cnicht &7gefunden werden!"),
        CMD_RECEIVER_NOT_PROVIDED("cmdReceiverNotProvided", "&cDu musst einen Empfänger angeben!"),
        CMD_RECEIVER_NOT_PLAYER("cmdReceiverNotPlayer", "Der Empfänger &d%1 &7ist kein Spieler!"),
        CMD_MESSAGE_NOT_PROVIDED("cmdMessageNotProvided", "&cDu musst eine Nachricht angeben!"),
        CMD_RECEIVER_NOT_ONLINE("cmdReceiverNotOnline", "Der Spieler &d%1 &7ist &cnicht &7online!"),
        CMD_ONLY_FOR_PLAYERS("cmdOnlyForPlayers", "&cDieser Befehl kann nur von Spielern ausgeführt werden!"),
        CMD_PLAYER_NOT_PROVIDED("cmdPlayerNotProvided", "&cDu musst einen Spieler angeben!"),
        CMD_PREFIX_NOT_PROVIDED("cmdPrefixNotProvided", "&cDu musst einen Prefix angeben!"),
        CMD_PLAYER_NEVER_ONLINE("cmdPlayerNeverOnline", "Der Spieler &d%1 &7war noch &cnie &7online!"),
        CMD_COLOR_NOT_PROVIDED("cmdColorNotProvided", "&cDu musst eine Farbe angeben!"),
        NO_PERMISSION("noPermission", "&cDu hast keine Berechtigung!"),
        CMD_MONEY_UNKNOWN_SUBCOMMAND("cmdMoneyUnknownSubcommand", "Der Unterbefehl &d%1 &7konnte &cnicht &7gefunden werden!"),
        CMD_MONEY_AMOUNT_NOT_PROVIDED("cmdMoneyAmountNotProvided", "&cDu musst einen Geldbetrag angeben!"),
        CMD_MONEY_AMOUNT_NOT_VALID("cmdMoneyAmountNotValid", "&7Der Betrag &d%1 &7ist nicht gültig. Bitte wähle eine ganze Zahl!"),
        CMD_MONEY_PAY_NOT_ENOUGH_MONEY("cmdMoneyPayNotEnoughMoney", "&cDu hast nicht genug Geld um &a%1 &e€ &czu überweisen!"),
        CMD_MONEY_PAY_SELF("cmdMoneyPaySelf", "&cDu kannst dir nicht selbst Geld überweisen!"),
        CMD_MONEY_TOP_EMPTY("cmdMoneyTopEmpty", "&cDer Topliste ist leer!"),
        CMD_HOME_NOT_FOUND("cmdHomeNotFound", "&cDas Home &a%1 &cexistiert nicht!"),
        CMD_SETHOME_NOT_ENOUGH_HOMES("cmdSetHomeNotEnoughHomes", "&cDu hast nicht genug Homes um ein weiteres zu setzen!"),
        CMD_SETHOME_ALREADY_EXISTING("cmdSetHomeAlreadyExisting", "&cDas Home &a%1 &cexistiert bereits!"),
        CMD_BUYHOME_NOT_ENOUGH_MONEY("cmdBuyHomeNotEnoughMoney", "&cDu hast nicht genug Geld um ein Home zu kaufen!"),
        CMD_HOMENAME_NOT_PROVIDED("cmdHomeNameNotProvided", "&cDu musst einen Home Namen angeben!"),
        CMD_VIEWHOME_HOME_NOT_FOUND("cmdViewHomeHomeNotFound", "&cDas Home &a%1 &ckonnte nicht gefunden werden!"),
        ALREADY_OPEN_SAVE_REQUEST("alreadyOpenSaveRequest", "&cDu hast bereits eine Sicherungsanfrage offen!"),
        CMD_SAVE_TP_INVALID_ID("cmdSaveTpInvalidId", "&cDu musst eine gültige ID angeben!"),
        SAVE_AXE_NOT_VALID("saveAxeNotValid", "&cDu musst eine aktive Save-Axt nehmen!"),
        SAVE_REGION_NO_SELECTION("saveRegionNoSelection", "&cDu musst eine Region auswählen!"),
        SAVE_REGION_COLLISION("saveRegionCollision", "&cIn diesem Bereich befindet sich bereits eine andere Region"),
        GS_ADD_NAME_NOT_PROVIDED("gsAddNameNotProvided", "&cDu musst einen Spieler angeben, den du hinzufügen möchtest!"),
        GS_IN_NO_REGION("gsInNoRegion", "&cDu befindest dich auf keinem Grundstück!"),
        GS_ADD_PLAYER_ALREADY_MEMBER("gsAddPlayerAlreadyMember", "&cDer Spieler &a%1 &cist bereits Mitglied auf deinem Grundstück!"),
        GS_REMOVE_NAME_NOT_PROVIDED("gsRemoveNameNotProvided", "&cDu musst einen Spieler angeben, den du entfernen möchtest!"),
        GS_REMOVE_PLAYER_NOT_MEMBER("gsRemovePlayerNotMember", "&cDer Spieler &a%1 &cist kein Mitglied auf deinem Grundstück!"),
        GS_TRANSFER_NAME_NOT_PROVIDED("gsTransferNameNotProvided", "&cDu musst einen Spieler angeben, an den du das Grundstück übergeben möchtest."),
        GS_NO_PERMISSIONS_IN_REGION("gsNoPermissionsInRegion", "&cDu hast dafür hier nicht die Berechtigung!"),
        GS_TRANSFER_PLAYER_IS_ALREADY_OWNER("gsTransferPlayerIsAlreadyOwner", "&cDer Spieler &a%1 &cist bereits Inhaber dieses Grundstücks!"),
        GS_ADD_PLAYER_IS_OWNER("gsAddPlayerIsOwner", "&cDu kannst dich nicht selbst als Mitglied eintragen!"),
        CMD_RANK_NOT_PROVIDED("cmdRankNotProvided", "&cDu musst einen Rang angeben, welchen du vergeben möchtest!"),
        CMD_RANK_NOT_FOUND("cmdRankNotFound", "&cDer Rang &b%1 &ckonnte nicht gefunden werden!"),
        CMD_BAN_PLAYER_ALREADY_BANNED("cmdBanPlayerAlreadyBanned", "&cDer Spieler &a%1 &cist bereits gebannt!"),
        CMD_BAN_DURATION_NOT_PROVIDED("cmdBanDurationNotProvided", "&cDu musst eine Dauer angeben!"),
        CMD_BAN_DURATION_INVALID("cmdBanDurationInvalid", "&cDie Dauer &a%1 &cist nicht gültig."),
        CMD_BAN_PLAYER_NOT_BANNED("cmdBanPlayerNotBanned", "&cDer Spieler &a%1 &cist nicht gebannt!"),
        CMD_GM_NO_GAMEMODE_PROVIDED("cmdGmNoGamemodeProvided", "&cDu musst einen Gamemode angeben!"),
        CMD_PLAYER_NOT_ONLINE("cmdPlayerNotOnline", "&cDer Spieler &a%1 &cist nicht online!"),
        CMD_GM_GAMEMODE_INVALID("cmdGmGamemodeInvalid", "&cDer Gamemode &a%1 &cist nicht gültig!");

        private final String key;
        private final String def;

        Error(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getDefault() {
            return this.def;
        }

        @Override
        public String getConfigPrefix() {
            return "error";
        }
    }
    public enum Info implements BaseMessageType {
        CMD_LANGUAGE_SET("cmdLanguageSet", "Du hast deine Sprache &aerfolgreich &7zu &d%1 &7geändert."),
        CMD_PREFIX_SET("cmdPrefixSet", "Du hast den Prefix von &e%1 &aerfolgreich &7zu &d%2 &7geändert."),
        CMD_NAME_COLOR_SET("cmdNameColorSet", "Du hast die Namensfarbe von &e%1 &aerfolgreich &7zu %2%3 &7geändert."),
        CMD_DEFAULT_CHAT_COLOR_SET("cmdDefaultChatColorSet", "Du hast die Standard-Chatfarbe von &e%1 &aerfolgreich &7zu %2Farbe &7geändert."),
        CMD_YOUR_PLAYTIME("cmdYourPlaytime", "Deine Spielzeit: &a%1 &7Tage, &a%2 &7Stunden, &a%3 &7Minuten, &a%4 &7Sekunden"),
        CMD_OTHER_PLAYTIME("cmdOtherPlaytime", "Die Spielzeit von &e%1 &7beträgt &a%2 &7Tage, &a%3 &7Stunden, &a%4 &7Minuten, &a%5 &7Sekunden."),
        CMD_MONEY_DISPLAY_SELF("cmdMoneyDisplaySelf", "Dein Geld: &a%1 &e€"),
        CMD_MONEY_PAYED("cmdMoneyPayed", "Du hast &a%1 &e€ &7an &d%2 &7überwiesen."),
        CMD_MONEY_RECEIVED("cmdMoneyReceived", "Du hast &a%1 &e€ &7von &e%2 &7erhalten."),
        CMD_MONEY_DISPLAY_OTHER("cmdMoneyDisplayOther", "&e%1 &7hat &a%2 &e€"),
        CMD_MONEY_SET("cmdMoneySet", "Du hast das Geld von &e%1 &aerfolgreich &7zu &a%2 &e€ &7geändert."),
        CMD_MONEY_TOP_HEADER("cmdMoneyTopHeader", "Top 10 Spieler mit den meisten Geld"),
        CMD_MONEY_TOP_PLAYER("cmdMoneyTopPlayer", "&a#%1 &7- &e%2 &7- &a%3 &e€"),
        CMD_MONEY_ADD("cmdMoneyAdd", "Dem Kontostand von &e%1 &7wurden &a%2 &e€ &7hinzugefügt."),
        CMD_HOME_SET("cmdHomeSet","Du hast ein neues Home &a%1 &7erstellt."),
        CMD_HOME_TELEPORT("cmdHomeTeleport","Du hast dich zu dem Home &a%1 &7teleportiert."),
        CMD_SETHOME_BUYHOME("cmdSethomeBuyhome","Jetzt ein neues Home kaufen? @ClickableComponent<Text=&a[Kaufen]|ClickCommand=/buyhome|HoverText=Kaufen>"),
        CMD_SETHOME_OVERRIDE_EXISTING_HOME("cmdSethomeOverrideExistingHome","Möchtest du das Home trotzdem überschreiben? @ClickableComponent<Text=&c[Überschreiben]|ClickCommand=/sethome %1 confirm|HoverText=Überschreiben> @ClickableComponent<Text=&a[Neues Home kaufen]|ClickCommand=/buyhome|HoverText=Kaufen>"),
        CMD_BUYHOME("cmdBuyhome","Möchtest du dir für &a%1 &e€ &7ein neues Home kaufen? @ClickableComponent<Text=&a[Kaufen]|ClickCommand=/buyhome confirm|HoverText=Kaufen>"),
        CMD_BUYHOME_SUCCESS("cmdBuyhomeSuccess","Du hast dir für &a%1 &e€ &7ein neues Home gekauft. Du hast &a%2 &7freie Homes."),
        CMD_HOMES_NO_HOMES("cmdHomesNoHomes","Du hast keine Homes."),
        CMD_HOMES_HOMES("cmdHomesHomes","Deine Homes: %1"),
        CMD_VIEWHOME("cmdViewhome","&7%1: @ClickableComponent<Text=&a[Teleportieren]|ClickCommand=/home %1|HoverText=Teleportieren> @ClickableComponent<Text=&c[Löschen]|ClickCommand=/delhome %1|HoverText=Löschen>"),
        CMD_DELHOME_CONFIRM("cmdDelhomeConfirm","Möchtest du das Home &a%1 &7wirklich löschen? @ClickableComponent<Text=&c[Löschen]|ClickCommand=/delhome %1 confirm|HoverText=Löschen>"),
        CMD_DELHOME_DONE("cmdDelhomeDone","Das Home &a%1 &7wurde erfolgreich gelöscht."),
        CMD_DELHOME_MAYBE_WRONG_NAME("cmdDelhomeMaybeWrongName","Das Home &a%1 &7wurde nicht gefunden. Meintest du vielleicht &a%2&7? @ClickableComponent<Text=&c[Löschen]|ClickCommand=/delhome %2|HoverText=%2 Löschen>"),
        CMD_HOME_MAYBE_WRONG_NAME("cmdHomeMaybeWrongName","Das Home &a%1 &7konnte nicht gefunden werden. Meintest du vielleicht &a%2&7? @ClickableComponent<Text=&a[Teleportieren]|ClickCommand=/home %2|HoverText=Teleportieren>"),
        CMD_VIEWHOME_MAYBE_WRONG_NAME("cmdViewhomeMaybeWrongName","Das Home &a%1 &7konnte nicht gefunden werden. Meintest du vielleicht &a%2&7? @ClickableComponent<Text=&a[Anzeigen]|ClickCommand=/viewhome %2|HoverText=Anzeigen>"),
        GS_CLAIM_REQUEST("gsClaimRequest","&e%1 &7möchte ein Grundstück gesichert bekommen. @ClickableComponent<Text=&a[Teleportieren]|ClickCommand=/save tp %2|HoverText=Teleportieren>"),
        SAVE_REGION_SUCCESS("saveRegionSuccess","Du hast das Grundstück erfolgreich gesichert."),
        REGION_SAVED_SUCCESSFUL("regionSavedSuccessful","Dein Grundstück wurde erfolgreich gesichert!"),
        GS_ADD_ADDED_PLAYER_SUCCESSFUL("gsAddAddedPlayerSuccessful", "Du hast den Spieler &a%1 &7erfolgreich auf dein Grundstück hinzugefügt."),
        GS_REMOVE_REMOVED_PLAYER_SUCCESSFUL("gsRemoveRemovedPlayerSuccessful", "Du hast den Spieler &a%1 &7erfolgreich von deinem Grundstück entfernt."),
        GS_TRANSFER_SUCCESSFUL("gsTransferSuccessful", "Das Grundstück wurde &aerfolgreich &7an &c%1 &7 übergeben."),
        GS_DELETE_SUCCESSFUL("gsDeleteSuccessful", "Dein Grundstück wurde &aerfolgreich &7gelöscht."),
        GS_INFO_REGION_NAME("gsInfoRegionName", "&aRegion: &6%1"),
        GS_INFO_REGION_OWNERS("gsInfoRegionOwners", "&aBesitzer: &6%1"),
        GS_INFO_REGION_MEMBERS("gsInfoRegionMembers", "&aMitglieder: &6%1"),
        TAB_HEADER("tabHeader", "&6Paradubsch\n\n&7Spieler &8> &a%1\n"),
        TAB_FOOTER("tabFooter", "\n&7Hast du fragen? Wende dich an ein Teammitglied.\nDiscord: &ahttps://discord.gg/GgXaq6ubvp"),
        CMD_RANKED_SUCCESSFUL("cmdRankedSuccessful", "&aDu hast erfolgreich den Rang &6%1 &aan &b%2 &avergeben."),
        CMD_RANKED_UP_SUCCESSFUL("cmdRankedUpSuccessful", "&aHerzlichen Glückwunsch! Du bist in den Rang &6%1 &aaufgestiegen."),
        CMD_BAN_SUGGEST_UPDATE("cmdBanSuggestUpdate", "Möchtest du den Ban statdessen Updaten? @ClickableComponent<Text=&a[Update]|SuggestCommand=/ban update %1 |HoverText=Ban updaten>"),
        CMD_BAN_PLAYER_UNBANNED("cmdBanPlayerUnbanned", "&aDer Spieler &6%1 &awurde erfolgreich entbannt."),
        CMD_BAN_KICK_MESSAGE("cmdBanKickMessage", "&6» &4Paradubsch &6«\n\n&7Du wurdest gebannt!\n\n&7Grund: &e%1\n\n&7Dauer: &c%2\n\n&7Ban-Id: &a%3"),
        CMD_BAN_PLAYER_BANNED("cmdBanPlayerBanned", "&aDer Spieler &6%1 &awurde erfolgreich gebannt."),
        CMD_BAN_EDITED("cmdBanEdited", "&aDer Ban wurde erfolgreich bearbeitet.");

        private final String key;
        private final String def;

        Info(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getDefault() {
            return this.def;
        }

        @Override
        public String getConfigPrefix() {
            return "info";
        }
    }

    public enum Constant implements BaseMessageType {
        SERVER_CONSOLE("constantServerConsole", "&4&lServer Konsole"),
        MSG_TEMPLATE("constantMsgTemplate", "&8[&a%1 &7» &a%2&8] &d%3"),
        FROM_YOU("constantFromYou", "Du"),
        TO_YOU("constantToYou", "Dir"),
        CHAT_MESSAGE_TEMPLATE("constantChatMessageTemplate", "%1 &8| %2%3&r &8» %4%5"),
        CMD_HOMES_TEMPLATE("constantCmdHomesTemplate", "@ClickableComponent<Text=&a%1|ClickCommand=/viewhome %1|HoverText=Anzeigen>");

        private final String key;
        private final String def;

        Constant(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getDefault() {
            return this.def;
        }

        @Override
        public String getConfigPrefix() {
            return "constant";
        }
    }

    public enum Gui implements BaseMessageType {
        CLAIM_TITLE("guiClaimTitle", "&a&lGrundstück sichern?"),
        CANCEL("guiCancel", "&cAbbrechen"),
        GS_CLAIM_TITLE("guiGsClaimTitle", "&aSicherung des Grundstücks beantragen"),
        GS_CLAIM_LORE("guiGsClaimLore", "Unser Team wird über deine Anfrage informiert."),
        SAVE_CONFIRM_TITLE("guiSaveConfirmTitle", "&a&lGrundstück Daten überprüfen"),
        SAVE_CONFIRM_BUTTON_TITLE("guiSaveConfirmButtonTitle", "&a&lGrundstück sichern"),
        GS_TRANSFER_TITLE("guiGsTransferTitle", "&a&lGrundstück übertragen"),
        GS_TRANSFER_BUTTON_TITLE("guiGsTransferButtonTitle", "&a&lGrundstück übertragen"),
        GS_TRANSFER_BUTTON_LORE("guiGsTransferButtonLore", "&aHiermit überträgst du dein Grundstück an den Spieler &b%1"),
        GS_DELETE_TITLE("guiGsDeleteTitle", "&a&lGrundstück löschen?"),
        GS_DELETE_BUTTON_TITLE("guiGsDeleteButtonTitle", "&cGrundstück löschen"),
        GS_DELETE_BUTTON_LORE("guiGSDeleteButtonLore", "Dieser Vorgang kann &cnicht &7mehr rückgängig gemacht werden!");

        private final String key;
        private final String def;

        Gui(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getDefault() {
            return this.def;
        }

        @Override
        public String getConfigPrefix() {
            return "gui";
        }
    }
}
