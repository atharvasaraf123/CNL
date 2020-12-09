#include <bits/stdc++.h>
using namespace std;

int main() {
        
    // Get Frame Details
    int fs;
    cout << "\nEnter frame size: ";
    cin >> fs;
        
    //int * frame = new int [fs];
    vector<int> frame(fs);
        
    cout << "\nEnter frame: ";
    for (int i = 0; i < fs; i++)
        cin >> frame[i];
        
    // Get Generator Details
    int gs;
    cout << "\nEnter generator size: ";
    cin >> gs;
    
    //int * generator = new int [gs];
    vector<int> generator(gs);
    
    cout << "\nEnter generator: ";
    for (int i = 0; i < gs; i++)
        cin >> generator[i];

    cout << "\nSender side: ";
    cout << "\nFrame: ";
    for (auto it : frame)
        cout << it;

    cout << "\nGenerator: ";
    for (auto it : generator)
        cout << it;

    int rs = gs - 1;
    //frame.resize(fs + rs);

    // Appending zeroes
    cout << "\nAppending " << rs << " zeroes: ";
    for (int i = 0; i < rs; i++)
        frame.push_back(0);

    vector<int> temp = frame;
    for (auto it : temp)
        cout << it;

    for (int i = 0; i < fs; i++) {
        int j = 0, k = i;
        if (temp[k] >= generator[j])
            for (; j < gs; j++, k++)
                temp[k] = temp[k] ^ generator[j]; // XOR
    }

    vector<int> CRC;
    for (int i = 0, j = fs; i < rs; i++, j++)
        CRC.push_back(temp[j]);

    cout << "\nCRC bits: ";
    for (auto it : CRC)
        cout << it;

    vector<int> tf;
    
    for (int i = 0; i < fs; i++)
        tf.push_back(frame[i]);
    
    for (int i = fs, j = 0; i < fs + rs; i++,j++)
        tf.push_back(CRC[j]);
    
    cout << "\nTransmitted Frame: ";
    for (auto it : tf)
        cout << it;

    cout << "\n---RECEIVER'S SIDE---";
    cout << "\n\nReceived Frame: ";

    for (auto it : tf)
        cout << it;

    temp = tf;

    // Division
    for (int i = 0; i < fs + rs; i++) {
        int j = 0, k = i;
        if (temp[k] >= generator[j])
            for (; j < gs; j++, k++)
                temp[k] = temp[k] ^ generator[j];
    }

    vector<int> rem;

    for (int i = fs; i < fs + rs; i++)
        rem.push_back(temp[i]);

    bool flag = false;
    cout << "\nRemainder: ";
    for (auto it : rem) {
        cout << it;
        if (it)
            flag = true;
    }

    if (flag)
        cout << "\nNon-zero remainder, error detected!" << endl;
    else
        cout << "\nRemainder is zero, error not found!" << endl;
    return 0;
}
