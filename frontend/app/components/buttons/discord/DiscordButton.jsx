import { View, StyleSheet, Image, Text, TouchableHighlight } from "react-native"

export const DiscordButton = ({ onPress }) => {
    return (
        <TouchableHighlight style={styles.discordButton__touchable} onPress={onPress}>
            <View style={styles.discordButton}>
                {/* <View style={styles.discordButton__image__container}> */}
                    <Image resizeMode="contain" style={styles.discordButton__image}
                        source={require('../../../../assets/images/logos/discordLogo.png')} />
                {/* </View> */}
                <Text style={styles.discordButton__text}>Sign in with Discord</Text>
            </View>
        </TouchableHighlight>
    )
}

const styles = StyleSheet.create({
    discordButton: {
        backgroundColor: "purple",
        flexDirection: "row",
        borderRadius: 16*0.25,
        width: "100%",
        paddingTop: 5,
        paddingBottom: 5,
        paddingLeft: 12,
        paddingRight: 12
    }, discordButton__text: {
        color: "white",
        alignSelf: "center",
        textAlign: "center",
        paddingLeft: 8
    }, discordButton__image:
    {
        height: 32,
        width: 32,
        alignSelf: "center"
    }, discordButton__touchable:
    {
        borderRadius: 16*0.25
    }
});