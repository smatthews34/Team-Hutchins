from ast import arg
from http import server
import smtplib
from email.message import EmailMessage
import sys
import os

workingdirectory = os.getcwd()
print ('Number of Arguements:', len(sys.argv), 'arguments.')
print ('Argument List: ', str(sys.argv))

def message_alert():
    msg = EmailMessage()
    body = sys.argv[1]
    subject = sys.argv[2]
    to = sys.argv[3]
    msg.set_content(body)
    msg['subject']=subject
    msg['to'] = to
    user = "myschedulinghelper@gmail.com"
    msg['from'] = user
    password = "zylmswnnfqhrpsio"
    server = smtplib.SMTP("smtp.gmail.com", 587)
    server.starttls()
    server.login(user, password)
    server.send_message(msg)
    server.quit()

message_alert()
'''
def message_alert(subject, body, to):
    msg = EmailMessage()
    msg.set_content(body)
    msg['subject']=subject
    msg['to'] = to
    user = "myschedulinghelper@gmail.com"
    msg['from'] = user
    password = "zylmswnnfqhrpsio"
    server = smtplib.SMTP("smtp.gmail.com", 587)
    server.starttls()
    server.login(user, password)
    server.send_message(msg)
    server.quit()

if __name__ == '__main__':
    message_alert("Hey", "Hello World!", "9154748044@vtext.com")
'''