#include <bits/stdc++.h>
using namespace std;

int main () {
    cout << "PACKET ANALYZER" << endl;
    string value, sr_no, time, source, destination, info, protocol, len;

    string choices [] = {
        "1. IP",
        "2. UDP",
        "3. TCP",
        "4. ETH",
        "5. EXIT"
    };

    int count = -1, i = 0, choice;

    do
    {
        ifstream file("data.csv");
        count = -1;
        i = 0;
        cout << "\nChoose packet format" << endl;

        for (int i = 0 ; i < 5 ; i++)
            cout << choices[i] << endl;

        cin >> choice;

        string protocol_choice;

        switch (choice) {
            case 1: protocol_choice = "ICMPv6"; break;
            case 2: protocol_choice = "UDP"; break;
            case 3: protocol_choice = "TCP"; break;
            case 4: protocol_choice = "ARP"; break;
            default: return(0);
        }

        while (file.good()) {
            getline(file,sr_no,',');
            getline(file,time,',');
            getline(file,source,',');
            getline(file,destination,',');
            getline(file,protocol,',');
            getline(file,len,',');
            getline(file,info,'\n');

            protocol = string(protocol,1,protocol.length()-2);

            if (protocol == "Protocol" || protocol == protocol_choice) {
                cout << setw(4) << left << i++;
                cout << setw(12)<< left << string( time, 1, time.length()-2 );
                cout << setw(30)<< left << string( source, 1, source.length()-2 );
                cout << setw(30)<< left << string( destination, 1, destination.length()-2 );
                cout << setw(8) << left << protocol;
                cout << setw(8) << left << string( len, 1, len.length()-2 );
                cout <<  string( info, 1, info.length()-2 ) << "\n";
                count++;
            }
        }
        file.close();
        cout << "\nTotal Packet Count: " << count;
        
    } while (choice != 5);
    return(0);
}