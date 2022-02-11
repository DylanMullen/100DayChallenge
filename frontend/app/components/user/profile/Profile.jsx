
import { ScrollView, View } from 'react-native';
import { ProfileBody } from './body/ProfileBody';
import { ProfileHeader } from './ProfileHeader';
import tw from 'tailwind-react-native-classnames';

export const Profile = ({ }) => {

    return (
        // <ScrollView style={tw`flex-1 bg-red-200`}>
        // <ScrollView style={tw`flex-1 `}>
        <View style={tw`flex-1 bg-gray-300`}>
            <View style={tw`h-1/2`}>
                <ProfileHeader />
            </View>
            <View style={tw`h-1/2`}>
                <ProfileBody />
            </View>
        </View>
        // </ScrollView>

            // <ProfileHeader discordID={"discordID"} />
        // </ScrollView>
    );
}