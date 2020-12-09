import socket

while True:
    print("\nDNS LOOKUP")
    print("1. Get host by name")
    print("2. Get host by ip")
    print("3. Exit")
    choice = input("Enter your choice: ")

    if choice == "3":
        break
    
    inp = input("Enter name: ") if choice == "1" else input("Enter IP: ")

    output = socket.gethostbyname(inp) if choice == "1" else socket.gethostbyaddr(inp)
    
    print(output)