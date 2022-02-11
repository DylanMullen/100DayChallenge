import { StatusBar } from 'expo-status-bar';
import { SafeAreaView, StyleSheet, Text, View } from 'react-native';
import { Profile } from './app/components/user/profile/Profile';
import tw from 'tailwind-react-native-classnames'
import { NavigationContainer } from '@react-navigation/native';
import NavigationStack from './app/screens';

export default function App() {
  return (
    <NavigationContainer>
      <NavigationStack />
    </NavigationContainer>
  );
}