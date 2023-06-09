package de.paradubsch.paradubschmanager.util.lang;

import de.craftery.craftinglib.messaging.lang.BaseMessageType;

public class Message {
    public enum Error implements BaseMessageType {
        CMD_LANGUAGE_NOT_PROVIDED("cmdLanguageNotProvided", "&cDu musst eine Sprache angeben!"),
        CMD_LANGUAGE_NOT_FOUND("cmdLanguageNotFound", "Die Sprache &d%1 &7konnte &cnicht &7gefunden werden!"),
        CMD_RECEIVER_NOT_PROVIDED("cmdReceiverNotProvided", "&cDu musst einen Empfänger angeben!"),
        CMD_RECEIVER_NOT_PLAYER("cmdReceiverNotPlayer", "Der Empfänger &d%1 &7ist kein Spieler!"),
        CMD_MESSAGE_NOT_PROVIDED("cmdMessageNotProvided", "&cDu musst eine Nachricht angeben!"),
        CMD_RECEIVER_NOT_ONLINE("cmdReceiverNotOnline", "Der Spieler &d%1 &7ist &cnicht &7online!"),
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
        CMD_MUTE_PLAYER_ALREADY_MUTED("cmdMutePlayerAlreadyMuted", "&cDer Spieler &a%1 &cist bereits gemutet!"),
        CMD_BAN_DURATION_NOT_PROVIDED("cmdBanDurationNotProvided", "&cDu musst eine Dauer angeben!"),
        CMD_MUTE_DURATION_NOT_PROVIDED("cmdMuteDurationNotProvided", "&cDu musst eine Dauer angeben!"),
        CMD_BAN_DURATION_INVALID("cmdBanDurationInvalid", "&cDie Dauer &a%1 &cist nicht gültig."),
        CMD_MUTE_DURATION_INVALID("cmdMuteDurationInvalid", "&cDie Dauer &a%1 &cist nicht gültig."),
        CMD_BAN_PLAYER_NOT_BANNED("cmdBanPlayerNotBanned", "&cDer Spieler &a%1 &cist nicht gebannt!"),
        CMD_MUTE_PLAYER_NOT_MUTED("cmdMutePlayerNotMuted", "&cDer Spieler &a%1 &cist nicht gemutet!"),
        CMD_GM_NO_GAMEMODE_PROVIDED("cmdGmNoGamemodeProvided", "&cDu musst einen Gamemode angeben!"),
        CMD_PLAYER_NOT_ONLINE("cmdPlayerNotOnline", "&cDer Spieler &a%1 &cist nicht online!"),
        CMD_GM_GAMEMODE_INVALID("cmdGmGamemodeInvalid", "&cDer Gamemode &a%1 &cist nicht gültig!"),
        CMD_WARP_NOT_PROVIDED("cmdWarpNotProvided", "&cDu musst einen Warp angeben!"),
        CMD_WARP_NOT_FOUND("cmdWarpNotFound", "&cDer Warp &b%1 &ckonnte nicht gefunden werden!"),
        CMD_WARPS_NO_WARPS("cmdWarpsNoWarps", "&cEs sind keine Warps vorhanden!"),
        CMD_PLAYER_ONLINE("cmdPlayerOnline", "&cDer Spieler &a%1 &cist online!"),
        CMD_REPLY_NO_REPLY_CANDIDATE("cmdReplyNoReplyCandidate", "&cDu hast keine aktive Unterhaltung!"),
        CMD_REPLY_CANDIDATE_OFFLINE("cmdReplyCandidateOffline", "&cDein Unterhaltungspartner ist bereits offline!"),
        CMD_SPEED_NOT_PROVIDED("cmdSpeedNotProvided", "&cDu musst eine Geschwindigkeit angeben!"),
        CMD_SPEED_NOT_VALID("cmdSpeedNotValid", "&cDie Geschwindigkeit &a%1 &cist nicht gültig!"),
        CMD_KICK_NAME_NOT_PROVIDED("cmdKickNameNotProvided", "&cDu musst einen Spieler angeben, den du kicken möchtest!"),
        CMD_KICK_PLAYER_NOT_IN_REGION("cmdKickPlayerNotInRegion", "&cDer Spieler &a%1 &cist nicht auf deinem Grundstück!"),
        CMD_KICK_YOU_GOT_KICKED("cmdKickYouGotKicked", "&cDu wurdest vom Grundstück geworfen!"),
        CMD_TPA_ALREADY_SENT("cmdTpaAlreadySent", "&cDu hast bereits eine Teleport-Anfrage an &a%1 &cgesendet!"),
        CMD_TPA_TIMEOUT("cmdTpaTimeout", "&cDeine Teleportationsanfrage an &6%1 &aist abgelaufen."),
        CMD_TPA_NOT_GOT("cmdTpaNotGot", "&cDu hast keine Teleportationsanfrage von &a%1 &cbekommen!"),
        CMD_TPA_NO_TPA_GOT("cmdTpaNoTpaGot", "&cDu hast keine Teleportationsanfrage von bekommen!"),
        CMD_TPA_SELF("cmdTpaSelf", "&cDu kannst dich nicht zur dir selbst teleportieren!"),
        CMD_RTP_NO_DESTINATION_FOUND("cmdRtpNoDestinationFound", "&cEs konnte kein Ziel gefunden werden!"),
        NOT_ENOUGH_MONEY("notEnoughMoney", "&cDafür hast du nicht genug Geld!"),
        CMD_GS_BACKUP_FAILED("cmdGsBackupFailed", "&cDas Backup konnte nicht erstellt werden!"),
        CMD_NIGHTVISION_WRONG_SYNTAX("cmdNightvisionWrongSyntax", "&cBitte verwende /nachtsicht <an/aus>"),
        CMD_NIGHTVISION_ALREADY_ON("cmdNightvisionAlreadyOn","&cDu hast bereits Nachtsicht."),
        CMD_NIGHTVISION_ALREADY_OFF("cmdNightvisionAlreadyOff","&cDu hast keine Nachtsicht."),
        CMD_BAZAAR_PRICE_NOT_PROVIDED("cmdBazaarPriceNotProvided", "&cDu musst einen Preis angeben!"),
        CMD_BAZAAR_CANNOT_SELL_THIS_ITEM("cmdBazaarConnotSellThisItem", "&cDu kannst dieses Item nicht auf dem Basar verkaufen!"),
        CMD_BAZAAR_NOT_ENOUGH_ITEMS("cmdBazaarNotEnoughItems", "&cDu brauchst mindestens &a%1&7x@ClickableComponent<Translatable=%2>&cum es auf dem Basar zu verkaufen!"),
        CMD_BAZAAR_INVALID_PRICE("cmdBazaarInvalidPrice", "&cDer Preis muss größer als &a%1&c sein und nicht größer als &a%2&c!"),
        CMD_TIMEVOTE_NOT_RUNNING("cmdVoteNotRunning","&cEs läuft grade keine Abstimmung."),
        CMD_TIMEVOTE_ALREADY_RUNNING("cmdTimevoteAlreadyRunning","&cEs läuft bereits eine Abstimmung."),
        CMD_TIMEVOTE_ALREADY_VOTED("cmdTimevoteAlreadyVoted","&cDu hast bereits an dieser Abstimmung teilgenommen."),
        CMD_TIMEVOTE_WRONG_SYNTAX("cmdTimevoteWrongSyntax","&cBitte verwende /votezeit vote <Ja/Nein>&c oder /votezeit create <Tag/Nacht>&c."),
        CMD_TIMEVOTE_COOLDOWN("cmdTimevoteCooldown","&cDu kannst erst in &a%1&c wieder eine Abstimmung starten."),
        CMD_GS_WHITELIST_MISSING_ARGUMENT("cmdGsWhitelistMissingArgument","&cBitte verwende /gs whitelist (<on/off>|<add/remove> <Spieler>)"),
        CMD_GS_WHITELIST_UNKNOWN_SUBCOMMAND("cmdGsWhitelistUnknownSubcommand","&cUnbekannter Subcommand &a%1&c."),
        CMD_GS_WHITELIST_OVERLAPPING_REGIONS("cmdGsWhitelistOverlappingRegions","&cAn dieser Stelle befinden sich mehrere Grundstücke. Das geht hier nicht."),
        GS_NOT_WHITELISTED("gsNotWhitelisted","&cDu bist auf diesem Grundstück nicht auf der Whitelist."),
        GS_BANNED("gsBanned","&cDu bist auf diesem Grundstück gebannt."),
        CMD_KICK_PLAYER_CANNOT_BE_KICKED("cmdKickPlayerCannotBeKicked","&cDer Spieler &a%1 &ckann nicht gekickt werden."),
        INVALID_KIT_SYNTAX("invalidKitSyntax","&cBitte verwende /kit"),
        INVALID_KIT_ID("invalidKitId","&cDas Kit &a%1 &cexistiert nicht."),
        KIT_GUI_NO_PERMISSION("kitGuiNoPermission","&cDu hast keine Berechtigung, dieses Kit zu verwenden."),
        KIT_COLLECT_GUI_INVENTORY_FULL("kitCollectGuiInventoryFull","&cDein Inventar ist voll."),
        CMD_BACKPACK_CANT_GET_OPENED("cmdBackpackCantGetOpened","&cDer Rucksack kann gerade nicht geöffnet werden."),
        COLLECTABLE_TYPE_NOT_FOUND("collectableTypeNotFound","&cDer Typ &a%1 &cexistiert nicht."),
        ID_NOT_PROVIDED("idNotProvided","&cDu musst eine gültige ID angeben."),
        BLOCK_IS_ALREADY_COLLECTABLE("blockIsAlreadyCollectable","&cDieser Block ist bereits ein Sammelpunkt."),
        COLLECTABLE_ALREADY_COLLECTED("collectableAlreadyCollected","&cDu hast das bereits gesammelt."),
        COLLECTABLE_NOT_COLLECTED("collectableNotCollected","&cDies konnte nicht gesammelt werden. Bitte versuche es später nochmal.");

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
        CMD_MONEY_TOP_HEADER("cmdMoneyTopHeader", "Top 10 Spieler mit dem meisten Geld"),
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
        TAB_HEADER("tabHeader", "&6Paradubsch<newline><newline>&7Spieler &8> &a%1<newline> &7Welt &8> %2 <newline>"),
        TAB_FOOTER("tabFooter", "<newline>&7Hast du fragen? Wende dich an ein Teammitglied.<newline>Discord: &ahttps://discord.gg/GgXaq6ubvp"),
        CMD_RANKED_SUCCESSFUL("cmdRankedSuccessful", "&aDu hast erfolgreich den Rang &6%1 &aan &b%2 &avergeben."),
        CMD_RANKED_UP_SUCCESSFUL("cmdRankedUpSuccessful", "&aHerzlichen Glückwunsch! Du bist in den Rang &6%1 &aaufgestiegen."),
        CMD_BAN_SUGGEST_UPDATE("cmdBanSuggestUpdate", "Möchtest du den Ban statdessen Updaten? @ClickableComponent<Text=&a[Update]|SuggestCommand=/ban update %1 |HoverText=Ban updaten>"),
        CMD_MUTE_SUGGEST_UPDATE("cmdMuteSuggestUpdate", "Möchtest du den Mute statdessen Updaten? @ClickableComponent<Text=&a[Update]|SuggestCommand=/mute update %1 |HoverText=Mute updaten>"),
        CMD_BAN_PLAYER_UNBANNED("cmdBanPlayerUnbanned", "&aDer Spieler &6%1 &awurde erfolgreich entbannt."),
        CMD_MUTE_PLAYER_UNMUTED("cmdMutePlayerUnmuted", "&aDer Spieler &6%1 &awurde erfolgreich entmutet."),
        CMD_BAN_KICK_MESSAGE("cmdBanKickMessage", "&6» &4Paradubsch &6«<newline><newline>&7Du wurdest gebannt!<newline><newline>&7Grund: &e%1<newline><newline>&7Dauer: &c%2<newline><newline>&7Ban-Id: &a%3"),
        CMD_BAN_PLAYER_BANNED("cmdBanPlayerBanned", "&aDer Spieler &6%1 &awurde erfolgreich gebannt."),
        CMD_MUTE_PLAYER_MUTED("cmdMutePlayerMuted", "&aDer Spieler &6%1 &awurde erfolgreich gemutet."),
        CMD_BAN_EDITED("cmdBanEdited", "&aDer Ban wurde erfolgreich bearbeitet."),
        CMD_MUTE_EDITED("cmdMuteEdited", "&aDer Mute wurde erfolgreich bearbeitet."),
        CMD_WARN_KICK_MESSAGE("cmdWarnKickMessage", "&6» &4Paradubsch &6«<newline><newline>&7Du wurdest vom Server geworfen!<newline><newline>&7Grund: &e%1<newline><newline>&7Warn-Id: &a%2"),
        CMD_BAN_PLAYER_WARNED("cmdBanPlayerWarned", "&aDer Spieler &6%1 &awurde erfolgreich gewarnt."),
        CMD_WARP_CREATED("cmdWarpCreated", "&aDer Warp &6%1 &awurde erfolgreich erstellt."),
        CMD_WARP_TELEPORTED("cmdWarpTeleported", "&aDu wurdest erfolgreich zum Warp &6%1 &ateleportiert."),
        CMD_WARP_DELETED("cmdWarpDeleted", "&aDer Warp &6%1 &awurde erfolgreich gelöscht."),
        CMD_WARPS_WARPS("cmdWarpsWarps", "Warps: %1"),
        CMD_GAMEMODE_CHANGED("cmdGamemodeChanged", "&aDer Spielmodus von &6%1 &awurde erfolgreich zu &6%2 &agewechselt."),
        CMD_VANISH_DISABLED("cmdVanishDisabled", "&aDer Spieler &6%1 &aist nun wieder sichtbar."),
        CMD_VANISH_ENABLED("cmdVanishEnabled", "&aDer Spieler &6%1 &aist nun unsichtbar."),
        CMD_PLAYER_LAST_SEEN("cmdPlayerLastSeen", "Der Spieler &6%1 &7wurde zuletzt gesehen vor &a%2."),
        CMD_SPEED_SET("cmdSpeedSet", "&aDeine Geschwindigkeit &awurde erfolgreich auf &6%1 &agewechselt."),
        CMD_DAY_SET("cmdDaySet", "&aDu hast die Zeit erfolgreich auf &6Tag &agewechselt."),
        CMD_DAY_NIGHT("cmdDayNightSet", "&aDu hast die Zeit erfolgreich auf &6Nacht &agewechselt."),
        CMD_KICK_PLAYER_KICKED("cmdKickPlayerKicked", "&cDer Spieler &a%1 &cwurde gekickt!"),
        CMD_RUN_APPLIED_FIX_WORLD_HEIGHT("cmdRunAppliedFixWorldHeight", "&aDer Patch wurde erfolgreich auf dem Server angewendet. %1 betroffene Grundstücke!"),
        CMD_TPA_SENT("cmdTpaSent", "&aDu hast dem Spieler &6%1 &aeine Teleportationsanfrage gesendet."),
        CMD_TPA_RECEIVED("cmdTpaReceived", "&aDu hast eine Teleportationsanfrage von &6%1 &aerhalten. @ClickableComponent<Text=&7[&aAnnehmen&7]|SuggestCommand=/tpaccept %1 |HoverText=&aTeleportationsanfrage annehmen>"),
        CMD_ACCEPTED_TPA("cmdAcceptedTpa", "&aDu hast die Teleportationsanfrage von &6%1 &aangenommen."),
        CMD_ACCEPTED_TPA_RECEIVED("cmdAcceptedTpaReceived", "&6%1 &ahat deine Teleportationsanfrage angenommen."),
        CMD_TPA_CANCELED("cmdTpaCanceled", "&aDu hast deine Teleportationsanfrage an &6%1 &aabgebrochen."),
        JOB_CHANGED("jobChanged", "&aDu übst jetzt den Job &6%1 &aaus."),
        COMMAND_TIMEOUT("commandTimeout", "Dieser Befehl kann erst in &6%1 &7wieder verwendet werden, um Rechenkapazitäten zu sparen."),
        BUY_ORDER_PLACED("buyOrderPlaced", "&aDu hast einen Kaufangebot mit &a%1&7x &6%2 &afür &a%3&6€ &aerstellt."),
        CMD_SAVE_REQUESTS_NO_REQUESTS("cmdSaveRequestsNoRequests", "Es gibt derzeit keine offenen Anfragen."),
        CMD_SAVE_REQUESTS_REQUEST("cmdSaveRequestsRequest", "&6%2&7: &a%3 @ClickableComponent<Text=&7[&aBearbeiten&7]|SuggestCommand=/save tp %1 |HoverText=&aSaveanfrage bearbeiten> @ClickableComponent<Text=&7[&cLöschen&7]|SuggestCommand=/save delete %1 |HoverText=&cSaveanfrage Löschen>"),
        CMD_SAVE_DELETE_SUCCESS("cmdSaveDeleteSuccess", "&aDie Saveanfrage wurde erfolgreich gelöscht."),
        SOLD_ITEM_TRANSLATED("soldItemTranslatedTranslated", "Du hast &aerfolgreich &6%1&7x @ClickableComponent<Translatable=%2> &7für &a%3&6€ &7verkauft."),
        PLAYTIME_SET("playtimeSet", "&aDie Spielzeit von &6%1 &awurde erfolgreich auf &6%2 &ageändert."),
        SELL_ORDER_PLACED("sellOrderPlaced", "&aDu hast ein Verkaufsangebot mit &a%1&7x &6%2 &afür &a%3&6€ &aerstellt."),
        BOUGHT_ITEM_TRANSLATED("boughtItemTranslated", "Du hast &aerfolgreich &6%1&7x @ClickableComponent<Translatable=%2> &7für &a%3&6€ &7gekauft."),
        CMD_GS_BACKUP_CREATED("cmdGsBackupCreated", "&aDie Sicherung wurde erfolgreich erstellt. @ClickableComponent<Text=&7[&aHerunterladen&7]|OpenUrl=%1/downloadBackup/%2.schem|HoverText=Link öffnen>"),
        FEED_SUCCESS("feedSuccess", "&aDu wurdest erfolgreich gefüttert."),
        CMD_NIGHTVISION_ACTIVATED("cmdNightvisionActivated", "&aDu hast nun Nachtsicht."),
        CMD_NIGHTVISION_DEACTIVATED("cmdNightvisionDeactivated", "&aDu hast nun keine Nachtsicht mehr."),
        CMD_BAZAAR_PAYED_TAXES("cmdBazaarPayedTaxes", "Dafür hast du &a%1&6€ &7Steuern gezahlt."),
        CMD_ANVIL_OPENED("cmdAnvilOpened","&aDu hast deinen mobilen Amboss geöffnet."),
        CMD_LOOM_OPENED("cmdLoomOpened","&aDu hast deinen mobilen Webstuhl geöffnet."),
        CMD_SMITHING_OPENED("cmdSmithingOpened","&aDu hast deinen mobilen Schmiedetisch geöffnet."),
        CMD_STONECUTTER_OPENED("cmdStonecutterOpened","&aDu hast deine mobile Steinsäge geöffnet."),
        CMD_HAT_USE("cmdHatUse","&a Du hast dir einen neuen Hut aufgesetzt"),
        CMD_MUTE_GOT_MUTED_HEADER("cmdMuteGotMutedHeader","&cDu wurdest für &6%1 &cGemutet."),
        CMD_MUTE_GOT_MUTED_BODY("cmdMuteGotMutedBody","&cGrund: &6%1"),
        PERMA_MUTED("permaMuted","&cDu bist permanent gemutet. Du kannst nichts mehr in den Chat schreiben."),
        MUTED_HEADER("mutedHeader","&cDu bist noch für &6%1 &cgemutet."),
        MUTED_BODY("mutedBody","&cGrund: &6%1"),
        VOTE_STARTED("voteStarted","&7Die Spielzeit von &a%1&7 soll auf &6%2&7 geändert werden.<newline>Jetzt Abstimmen: @ClickableComponent<Text=&7[&aJa&7]|SuggestCommand=/votezeit vote Ja|HoverText=&7Für &aja &7stimmen.> @ClickableComponent<Text=&7[&cNein&7]|SuggestCommand=/votezeit vote Nein|HoverText=&7Für &cnein &7stimmen.>"),
        CMD_VOTE_VOTED_YES("cmdVoteVotedYes","&7Du hast &afür&7 eine Änderung der Spielzeit gestimmt."),
        CMD_VOTE_VOTED_NO("cmdVoteVotedNo","&7Du hast &cgegen&7 eine änderung der Spielzeit gestimmt."),
        VOTE_DECLINED("voteDeclined","&7Die Community hat sich gegen eine andere Tageszeit entschieden."),
        VOTE_TIME_CHANGE("voteTimeChange","&7Die Zeit in &a%1&7 wurde auf &a%2&7 geändert."),
        CMD_GS_WHITELIST_ENABLED("cmdGsWhitelistEnabled","&aDie Whitelist wurde aktiviert."),
        CMD_GS_WHITELIST_DISABLED("cmdGsWhitelistDisabled","&aDie Whitelist wurde deaktiviert."),
        CMD_GS_WHITELIST_ADD("cmdGsWhitelistAdd","&aDer Spieler &6%1 &awurde zur Whitelist hinzugefügt."),
        CMD_GS_WHITELIST_LIST("cmdGsWhitelistList","&aDie Whitelist: &6%1"),
        CMD_GS_WHITELIST_REMOVE("cmdGsWhitelistRemove","&aDer Spieler &6%1 &awurde von der Whitelist entfernt."),
        CMD_GS_BAN_SUCCESS("cmdGsBanSuccess","&aDer Spieler &6%1 &awurde erfolgreich vom Grundstück gebannt."),
        CMD_GS_BAN_BANNED("cmdGsBanBanned","&cDu wurdest vom Grundstück gebannt."),
        CMD_GS_PARDON_SUCCESS("cmdGsPardonSuccess","&aDer Spieler &6%1 &awurde erfolgreich vom Grundstück entbannt."),
        GS_INFO_REGION_WHITELIST_ENABLED("gsInfoRegionWhitelistEnabled","&7Die Whitelist ist &aaktiviert&7."),
        GS_INFO_REGION_WHITELIST_MEMBERS("gsInfoRegionWhitelistMembers","&7Whitelist Mitglieder: &6%1"),
        GS_INFO_REGION_BANNED_PLAYERS("gsInfoRegionBannedPlayers","&7Gebannte Spieler: &6%1"),
        KIT_EDITED("kitEdited","&aDer Kit &6%1 &awurde erfolgreich bearbeitet."),
        REGISTERED_COLLECTABLES("registeredCollectables","&7Es gibt folgende Sammelkategorien: &a%1"),
        COLLECTABLE_LIST_HEADER("collectableListHeader","&7Sammelstücke in Kategore: &a%1 &7:"),
        COLLECTABLE_LIST_ENTITY("collectableListEntity","&7ID: &a%1 @ClickableComponent<Text=&7[&aTP&7]|SuggestCommand=/collectables tp %1 |HoverText=&aTeleportieren> @ClickableComponent<Text=&7[&cLöschen&7]|SuggestCommand=/collectables remove %1 |HoverText=&cLöschen>"),
        TELEPORTED_TO_COLLECTABLE("teleportedToCollectable","&aDu wurdest zu dem Sammelstück &6%1&a teleportiert."),
        COLLECTABLE_REMOVED("collectableRemoved","&aDas Sammelstück &6%1 &awurde erfolgreich gelöscht."),
        ADD_MODE_DISABLED("addModeDisabled","&aDer Add-Modus wurde deaktiviert."),
        ADD_MODE_ENABLED("addModeEnabled","&aDer Add-Modus für die Gruppe &6%1 &awurde aktiviert. Clicke einfach einen Block an, um ihn als Sammelobjekt hinzuzufügen."),
        COLLECTABLE_ADDED("collectableAdded","&aDas Sammelstück wurde erfolgreich zur Kollektion &6%1 &ahinzugefügt.");

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
        CMD_HOMES_TEMPLATE("constantCmdHomesTemplate", "@ClickableComponent<Text=&a%1|ClickCommand=/viewhome %1|HoverText=Anzeigen>"),
        PERMANENT("constantPermanent", "Permanent"),
        DAY_SINGULAR("constantDaySingular", "Tag"),
        DAY_PLURAL("constantDayPlural", "Tage"),
        HOUR_SINGULAR("constantHourSingular", "Stunde"),
        HOUR_PLURAL("constantHourPlural", "Stunden"),
        MINUTE_SINGULAR("constantMinuteSingular", "Minute"),
        MINUTE_PLURAL("constantMinutePlural", "Minuten"),
        SECOND_SINGULAR("constantSecondSingular", "Sekunde"),
        SECOND_PLURAL("constantSecondPlural", "Sekunden"),
        CMD_WARP_TEMPLATE("constantCmdWarpTemplate", "@ClickableComponent<Text=&a%1|ClickCommand=/warp %1|HoverText=Teleportieren>"),
        BLANK("constantBlank", "%1"),
        LUMBERJACK("constantLumberjack", "Holzfäller"),
        COLLECTOR("constantCollector", "Sammler"),
        HUNTER("constantHunter", "Jäger"),
        FARMER("constantFarmer", "Bauer"),
        INSERT_NUMBER("constantInsertNumber", "Zahl eingeben"),
        OBJECT_DUMP("constantObjectDump", "@ObjectDump<%1>"),
        YOU_BUY("constantYouBuy", "Du kaufst:"),
        YOU_SELL("constantYouSell", "Du verkaufst:"),
        BLANK_TRANSLATABLE("constantBlankTranslatable", "@ClickableComponent<Translatable=%1>"),
        PAGE("constantPage", "Seite %1"),
        SPAWN_WORLD("constantSpawnWorld","&bSpawn"),
        BUILDING_WORLD("constantBauwelt","&6Bauwelt"),
        WORLD_THE_END("constantEnd","&dEnd"),
        WORLD_NETHER("constantNether","&cNether"),
        FARMING_WORLD("constantFarmwelt","&aFarmwelt"),
        EVENT_WORLD("constantEventwelt","&aEventwelt"),
        NIGHT("constantNight","Nacht");

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
        BACKPACK_TITLE("guiBackpackTitle", "&6&lBackpack"),
        CLAIM_TITLE("guiClaimTitle", "&a&lGrundstück sichern?"),
        CANCEL("guiCancel", "&cAbbrechen"),
        GS_CLAIM_TITLE("guiGsClaimTitle", "&aSicherung des Grundstücks beantragen"),
        GS_CLAIM_LORE("guiGsClaimLore", "Unser Team wird über deine Anfrage informiert."),
        SAVE_CONFIRM_TITLE("guiSaveConfirmTitle", "&a&lDaten überprüfen"),
        SAVE_CONFIRM_BUTTON_TITLE("guiSaveConfirmButtonTitle", "&a&lGrundstück sichern"),
        GS_TRANSFER_TITLE("guiGsTransferTitle", "&a&lGrundstück übertragen"),
        GS_TRANSFER_BUTTON_TITLE("guiGsTransferButtonTitle", "&a&lGrundstück übertragen"),
        GS_TRANSFER_BUTTON_LORE("guiGsTransferButtonLore", "&aHiermit überträgst du dein Grundstück an den Spieler &b%1"),
        GS_DELETE_TITLE("guiGsDeleteTitle", "&a&lGrundstück löschen?"),
        GS_DELETE_BUTTON_TITLE("guiGsDeleteButtonTitle", "&cGrundstück löschen"),
        GS_DELETE_BUTTON_LORE("guiGSDeleteButtonLore", "Dieser Vorgang kann &cnicht &7mehr rückgängig gemacht werden!"),
        JOB_GUI_TITLE("guiJobGuiTitle", "&a&lJobs"),
        JOB_MINER_TITLE("guiJobMinerTitle", "&7&lMiner"),
        JOB_MINER_LORE("guiJobMinerLore", "&aDu bekommst Geld indem du Erze farmst."),
        JOB_LUMBERJACK_TITLE("guiJobLumberjackTitle", "&7&lHolzfäller"),
        JOB_LUMBERJACK_LORE("guiJobLumberjackLore", "&aDu bekommst Geld indem du Holz farmst."),
        JOB_COLLECTOR_TITLE("guiJobCollectorTitle", "&7&lSammler"),
        JOB_COLLECTOR_LORE("guiJobCollectorLore", "&aDu bekommst Geld indem du Pilze und Blumen sammelst."),
        JOB_HUNTER_TITLE("guiJobHunterTitle", "&7&lJäger"),
        JOB_HUNTER_LORE("guiJobHunterLore", "&aDu bekommst Geld indem du Tiere und Monster tötest."),
        JOB_FARMER_TITLE("guiJobFarmerTitle", "&7&lBauer"),
        JOB_FARMER_LORE("guiJobFarmerLore", "&aDu bekommst Geld indem du die Ernte einfährst."),
        BAZAAR_MAIN_TITLE("guiBazaarMainTitle", "&a&lBazaar"),
        RTP_TITLE("guiRtpTitle", "&a&lRandom Teleport"),
        BACK("guiBack", "&cZurück"),
        PLACE_BUY_ORDER("guiPlaceBuyOrder", "&a&lKaufangebot stellen"),
        BUY_ORDER_TITLE("guiBuyOrderTitle", "&a&lKaufangebot erstellen"),
        AMOUNT("guiAmount", "&a&lAnzahl"),
        AMOUNT_LORE("guiAmountLore", "&7Anzahl: &a%1"),
        INVALID_AMOUNT_LORE("guiInvalidAmountLore", "&cUngültige Anzahl"),
        INVALID_AMOUNT_LORE_MULTIPLE("guiInvalidAmountLoreMultiple", "&cDie Anzahl muss ein Vielfaches von %1 sein"),
        PRICE("guiPrice", "&6&lPreis pro &a&l%1"),
        PRICE_LORE("guiPriceLore", "&7Preis: &a%1&6€"),
        INVALID_PRICE_LORE("guiInvalidPriceLore", "&cUngültiger Preis"),
        PRICE_TO_HIGH("guiPriceToHigh", "&cDu kannst das bereits direkt für &a%1 &6€ &ckaufen."),
        PRICE_TO_LOW_1("guiPriceToLow1", "&cDu musst mehr verlangen, da der Server dieses"),
        PRICE_TO_LOW_2("guiPriceToLow2", "&cItem bereits ab &a%1 &6€ &cautomatisch kauft."),
        PRICE_LORE_WITH_DESCRIPTION("guiPriceLoreWithDescription", "&7Preis: &a%1&6€ &7(&a%2 &7mal &a%3&6€&7)"),
        TAXES_LORE("guiTaxesLore", "&7Steuern: &a+%1&6€"),
        FINAL_PRICE_LORE("guiFinalPriceLore", "&7Gesamtpreis: &a%1&6€"),
        NOT_ENOUGH_MONEY("guiNotEnoughMoney", "&cDu hast nicht genug Geld."),
        SELL_INSTANT_TITLE("guiSellInstantTitle", "&a&lSofort verkaufen"),
        PRICE_PER_UNIT_LORE_TRANSLATED("guiPricePerUnitLoreTranslated", "&7Preis für &a%1 &7Einheiten @ClickableComponent<Translatable=%2>"),
        SELL_INSTANT_LORE("guiSellInstantLore", "&a%1&6x &7für &a%2&6€"),
        SERVER_PRICE_LORE("guiServerPriceLore", "&7Server-Preis: &a%1&6€"),
        AUCTION_NO_LONGER_AVAILABLE("guiAuctionNoLongerAvailable", "&cDieses Gebot ist nicht mehr verfügbar."),
        NOT_ENOUGH_ITEMS_TRANSLATED("guiNotEnoughItemsTranslated", "&cDu hast nicht genug @ClickableComponent<Translatable=%1>&c!"),
        BAZAAR_COLLECT_TITLE("guiBazaarCollectTitle", "&a&lItems einsammeln"),
        CLICK_TO_COLLECT("guiClickToCollect", "&aKlicke hier um die Items einzusammeln"),
        COLLECTABLE_AMOUNT("guiCollectableAmount", "&6%1 &7Items abholbereit."),
        PLACE_SELL_ORDER("guiPlaceSellOrder", "&a&lVerkaufsangebot stellen"),
        SELL_ORDER_TITLE("guiSellOrderTitle", "&a&lVerkaufsangebot erstellen"),
        SELL_PRICE_TO_HIGH("guiPriceToHigh", "&cDer Server bietet das bereits für &a%1 &6€ &can."),
        SELL_PRICE_TOO_LOW("guiSellPriceTooLow", "&cDer Server kauft das bereits für &a%1 &6€"),
        SELL_ORDER_TAXES("guiSellOrderTaxesLore", "&7Du zahlst: &a%1&6€ &7Steuern"),
        BUY_INSTANT_TITLE("guiBuyInstantTitle", "&a&lSofort kaufen"),
        INCOME_PER_UNIT_LORE_TRANSLATED("guiIncomePerUnitLoreTranslated", "&7Ertrag für &a%1 &7Einheiten @ClickableComponent<Translatable=%2>"),
        AUCTION_JUST_TAKEN("guiAuctionJustTaken", "&cDieses Gebot wurde gerade von jemand anderem gekauft."),
        NOT_ENOUGH_INVENTORY_SPACE("guiNotEnoughInventorySpace", "&cDu hast nicht genug Platz im Inventar."),
        BACK_PAGE("guiBackPage", "Seite zurück"),
        NEXT_PAGE("guiNextPage", "Nächste Seite"),
        YOU_ARE_IN_WORLD("guiYouAreInWorld", "&7Du befindest dich in der Welt"),
        REQUEST_BY("guiRequestBy", "&7Anfrage von"),
        PLOT_SIZE("guiPlotSize", "&7Grundstücksgröße"),
        KIT_GUI_TITLE("guiKitGuiTitle", "&a&lKits"),
        KIT_GUI_KIT_1_NAME("guiKitGuiKit1Name", "&a&lStarter Kit"),
        KIT_GUI_KIT_2_NAME("guiKitGuiKit2Name", "&a&lFortgeschrittenes Kit"),
        KIT_GUI_KIT_3_NAME("guiKitGuiKit3Name", "&a&lProfi Kit"),
        KIT_GUI_KIT_4_NAME("guiKitGuiKit4Name", "&a&lJetpack/Rockets"),
        KIT_GUI_KIT_5_NAME("guiKitGuiKit5Name", "&a&lAlchemy Set"),
        KIT_GUI_KIT_1_LORE_1("guiKitGuiKit1Lore1", "&7Starter Stuff für Spieler"),
        KIT_GUI_KIT_1_LORE_2("guiKitGuiKit1Lore2", "&aAlle 7 Tage einlösbar"),
        KIT_GUI_KIT_2_LORE_1("guiKitGuiKit2Lore1", "&7Eisenwerkzeuge ab dem Copper Rang"),
        KIT_GUI_KIT_2_LORE_2("guiKitGuiKit2Lore2", "&aAlle 7 Tage einlösbar"),
        KIT_GUI_KIT_3_LORE_1("guiKitGuiKit3Lore1", "&7Diamantwerkzeuge ab dem Amethyst Rang"),
        KIT_GUI_KIT_3_LORE_2("guiKitGuiKit3Lore2", "&aAlle 14 Tage einlösbar"),
        KIT_GUI_KIT_4_LORE_1("guiKitGuiKit4Lore1", "&7Jetpack und Rockets"),
        KIT_GUI_KIT_4_LORE_2("guiKitGuiKit4Lore2", "&aAlle 3 Tage einlösbar"),
        KIT_GUI_KIT_5_LORE_1("guiKitGuiKit5Lore1", "&7Alles was man zum brauen braucht"),
        KIT_GUI_KIT_5_LORE_2("guiKitGuiKit5Lore2", "&aAlle 7 Tage einlösbar"),
        KIT_COLLECT_GUI_TITLE("guiKitCollectGuiTitle", "&a&lItems einsammeln");

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
